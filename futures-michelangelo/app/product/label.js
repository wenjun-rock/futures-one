'use strict';

angular.module('miche.label', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/product/label/:id', {
    templateUrl: 'product/label.html',
    controller: 'micheLabelCtrl'
  });
}])

.controller('micheLabelCtrl', ['$scope', '$routeParams', '$http', '$interval', 'CF',
  function($scope, $routeParams, $http, $interval, CF) {

    var preurl = CF.preurl;

    $scope.ctrl = {
      id: Number($routeParams.id)
    };

    $scope.initChart = function() {

      if ($scope.ctrl.refresh) {
        var re = $scope.ctrl.refresh;
        $scope.ctrl.refresh = null;
        $interval.cancel(re);
      }

      $http.get(preurl + '/product/price-label-lastday?id=' + $scope.ctrl.id).success(function(lastDayLine) {

        lastDayLine = lastDayLine.filter(function(ele) {
          if (ele.prices) {
            return true;
          } else {
            return false;
          }
        });

        $scope.ctrl.latestTime = lastDayLine.map(function(ele) {
          return ele.prices[ele.prices.length - 1].d;
        }).reduce(function(prev, next) {
          return prev < next ? next : prev;
        }, 0);

        $scope.chartinfo = [];
        var seriesLine = lastDayLine.map(function(input) {
          var basePrice = input.prices[0].p;
          $scope.chartinfo.push({
            code: input.code,
            name: input.name,
            basePrice: basePrice
          });
          input.prices.splice(0, 1);
          return {
            name: input.name,
            data: input.prices.filter(function(element) {
              return element.p > 0;
            }).map(function(element) {
              return [element.d, parseFloat(((element.p - basePrice) / basePrice * 100).toFixed(2))];
            })
          };
        });

        $scope.realtimeChart = $('#realtimeChart').highcharts({
          chart: {
            zoomType: 'x'
          },
          title: {
            text: '实时走势'
          },
          credits: {
            enabled: false
          },
          xAxis: {
            type: "datetime"
          },
          yAxis: {
            title: {
              text: '涨幅'
            },
            labels: {
              format: '{value} %',
            }
          },
          tooltip: {
            shared: true,
            crosshairs: true
          },
          series: seriesLine
        });

        $scope.ctrl.refresh = $interval(function() {
          $http.get(preurl + '/product/price-latest').success(function(latest) {
            if (latest.datetime > $scope.ctrl.latestTime) {
              $scope.ctrl.latestTime = latest.datetime;
              var chart = $('#realtimeChart').highcharts();
              chart.series.forEach(function(series, index) {
                var info = $scope.chartinfo[index];
                var unitData = latest.unitDataMap[info.code];
                if (unitData) {
                  var price = parseFloat(((unitData.price - info.basePrice) / info.basePrice * 100).toFixed(2));
                  series.addPoint([latest.datetime, price], false);
                }
              });
              chart.redraw();
            }
          });
        }, 10000);

      });
    }

    $scope.$on('$destroy', function() {
      if ($scope.ctrl.refresh) {
        $interval.cancel($scope.ctrl.refresh);
      }
    });

    $scope.initTable = function() {
      $http.get(preurl + '/product/price-label-aggre?id=' + $scope.ctrl.id).success(function(data) {

        $('#example').dataTable({
          "data": data,
          "destroy": true,
          "paging": false,
          "searching": false,
          "info": false,
          "columns": [{
            "data": "code"
          }, {
            "data": "name"
          }, {
            "data": "price"
          }, {
            "data": "last1RIncPct"
          }, {
            "data": "last5RIncPct"
          }, {
            "data": "last10RIncPct"
          }, {
            "data": "last30RIncPct"
          }, {
            "data": "last60RIncPct"
          }, {
            "data": "last1KIncPct"
          }, {
            "data": "last5KIncPct"
          }, {
            "data": "last10KIncPct"
          }, {
            "data": "last30KIncPct"
          }, {
            "data": "last60KIncPct"
          }],
          "columnDefs": [{
            "render": function(data, type, row) {
              return '<a href="#/product/code/' + row.code + '?from=' + $scope.ctrl.id + '">' + data + '</a>';
            },
            "targets": [0, 1]
          }, {
            "render": function(data, type, row) {
              return data.toLocaleString();
            },
            "targets": [2]
          }, {
            "render": function(data, type, row) {
              var out = (data * 100).toFixed(2);
              if (out > 0) {
                return '<span style="color:red">' + out + '</span>';
              } else if (out < 0) {
                return '<span style="color:green">' + out + '</span>';
              } else {
                return out;
              }
              return out;
            },
            "targets": [3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
          }]
        });
      });
    };

    $scope.refresh = function() {

      $scope.ctrl.labels.some(function(label) {
        if (label.id == $scope.ctrl.id) {
          $scope.ctrl.name = label.name;
          return true;
        }
      });

      $scope.initChart();
      $scope.initTable();
    };

    $http.get(preurl + '/product/labels').success(function(labels) {
      $scope.ctrl.labels = [{
        id: 0,
        name: '全部'
      }].concat(labels);

      $scope.refresh();
    });
  }
]);

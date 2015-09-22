'use strict';

angular.module('miche.product.list', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/product/label/:id', {
    templateUrl: 'product/product-list.html',
    controller: 'micheProductListCtrl'
  }).when('/products', {
    redirectTo: '/product/label/0'
  });
}])

.controller('micheProductListCtrl', ['$scope', '$routeParams', '$http', function($scope, $routeParams, $http) {

  var preurl = 'http://139.196.37.92:8000/futures-api';

  $scope.ctrl = {
    id: Number($routeParams.id)
  };

  $scope.initChart = function() {
    $http.get(preurl + '/product/lastday/label/' + $scope.ctrl.id).success(function(lastDayLine) {

      $scope.ctrl.latestTime = lastDayLine.map(function(ele) {
        return ele.data[ele.data.length - 1][0];
      }).reduce(function(prev, next) {
        return prev < next ? next : prev;
      }, 0);

      $scope.chartinfo = [];
      var seriesLine = lastDayLine.map(function(input) {
        var basePrice = input.data[0][1];
        $scope.chartinfo.push({
          code: input.code,
          name: input.name,
          basePrice: basePrice
        });
        input.data.splice(0, 1);
        return {
          name: input.name,
          data: input.data.map(function(element) {
            return [element[0], parseFloat(((element[1] - basePrice) / basePrice).toFixed(4))];
          })
        };
      });

      $scope.realtimeChart = $('#realtimeChart').highcharts({
        chart: {
          zoomType: 'x'
        },
        title: {
          text: '实时价格走势'
        },
        xAxis: {
          type: "datetime"
        },
        yAxis: {
          title: {
            text: '价格'
          }
        },
        tooltip: {
          shared: true,
          crosshairs: true
        },
        series: seriesLine
      });

      setInterval(function() {
        $http.get(preurl + '/product/latest').success(function(latest) {
          if (latest.datetime > $scope.ctrl.latestTime) {
            $scope.ctrl.latestTime = latest.datetime;
            var chart = $('#realtimeChart').highcharts();
            chart.series.forEach(function(series, index) {
              var info = $scope.chartinfo[index],
                point;
              latest.unitDataList.some(function(unitData) {
                if (unitData.code == info.code) {
                  point = [latest.datetime, parseFloat(((unitData.price - info.basePrice) / info.basePrice).toFixed(4))];
                  return true;
                } else {
                  return false;
                }
              });
              if (point) {
                series.addPoint(point, false);
              }
            });
            chart.redraw();
          }
        });
      }, 10000);

    });
  }

  $scope.refreshChart = $scope.initChart;

  $scope.refreshTable = function() {
    $http.get(preurl + '/product/price/label/' + $scope.ctrl.id).success(function(data) {

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
            return '<a href="#/product/code/' + row.code + '">' + data + '</a>';
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
    $.getJSON(preurl + '/product/price/label/' + $scope.ctrl.id, function(data) {

    });
  };

  $scope.refresh = function() {
    $scope.refreshChart();
    $scope.refreshTable();
  };

  $http.get(preurl + '/product/labels').success(function(labels) {
    $scope.ctrl.labels = [{
      id: 0,
      name: '全部'
    }].concat(labels);
  });

  $scope.refresh();

}]);

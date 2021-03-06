'use strict';

angular.module('miche.label.wall', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/product/labels', {
    templateUrl: 'product/label-wall.html',
    controller: 'micheLabelWallCtrl'
  });
}])

.controller('micheLabelWallCtrl', ['$scope', '$location', '$interval', 'micheHttp',
  function($scope, $location, $interval, micheHttp) {

    $scope.imax = {
      id: 0
    };

    micheHttp.get('/product/labels').success(function(labels) {
      $scope.labels = labels;
      var prodArr = [],
        prodTmp = {};
      labels.forEach(function(label) {
        label.products.forEach(function(prod) {
          if (!prodTmp[prod.code]) {
            prodTmp[prod.code] = true;
            prodArr.push(prod);
          }
        });
      });
      $scope.imax.labels = [{
        id: 0,
        name: '全部',
        products: prodArr
      }].concat(labels);

      micheHttp.get('/product/price-label-lastday', {
        id: 0
      }).success(function(lastDayLine) {

        lastDayLine = lastDayLine.filter(function(ele) {
          if (ele.prices) {
            return true;
          } else {
            return false;
          }
        });

        $scope.imax.latestTime = lastDayLine.map(function(ele) {
          return ele.prices[ele.prices.length - 1].d;
        }).reduce(function(prev, next) {
          return prev < next ? next : prev;
        }, 0);

        $scope.prodLineObj = {};
        var seriesLine = lastDayLine.forEach(function(input) {
          var basePrice = input.prices[0].p;
          input.prices.splice(0, 1);
          $scope.prodLineObj[input.code] = {
            name: input.name,
            basePrice: basePrice,
            data: input.prices.filter(function(element) {
              return element.p > 0;
            }).map(function(element) {
              return [element.d, parseFloat(((element.p - basePrice) / basePrice * 100).toFixed(2))];
            })
          };
        });

        $scope.labelHighchart = {};
        $scope.labels.forEach(function(label) {
          $scope.drawOneChart(label);
        });

        $scope.drawImaxChart();

      });
    });

    // define event handler
    $scope.toLabel = function(labelId) {
      $location.path('/product/label/' + labelId);
    };

    $scope.drawOneChart = function(label) {
      var maxFromD = 0;
      var labelSeries = label.products.map(function(product) {
        var prototype = $scope.prodLineObj[product.code];
        var fromD = prototype.data[0][0];
        var toD = prototype.data[prototype.data.length - 1][0];
        if (fromD > maxFromD) {
          maxFromD = fromD;
        }
        return {
          formD: fromD,
          toD: toD,
          name: prototype.name,
          basePrice: prototype.basePrice,
          data: [].concat(prototype.data)
        };
      });
      var highchart = $('#chart-label-' + label.id).highcharts({
        credits: {
          enabled: false
        },
        chart: {
          zoomType: 'x'
        },
        tooltip: {
          descOrder: true,
          shared: true,
          crosshairs: true,
          dateTimeLabelFormats: {
            second: '%Y-%m-%d %H:%M:%S',
            minute: '%Y-%m-%d %H:%M',
            hour: '%Y-%m-%d %H:%M',
            day: '%Y-%m-%d'
          }
        },
        xAxis: {
          type: "datetime",
          dateTimeLabelFormats: {
            millisecond: '%H:%M:%S.%L',
            second: '%H:%M:%S',
            minute: '%H:%M',
            hour: '%H:%M',
            day: '%m-%d',
            week: '%m-%d',
            month: '%Y-%m',
            Year: '%Y'
          }
        },
        yAxis: {
          labels: {
            format: '{value} %',
          },
          title: {
            text: '涨幅'
          }
        },
        title: {
          text: label.name
        },
        series: labelSeries
      });

      highchart = $('#chart-label-' + label.id).highcharts();
      highchart.series.forEach(function(series, index) {
        if (labelSeries[index].toD < maxFromD) {
          series.hide();
        }
      });

      $scope.labelHighchart[label.id] = {
        label: label,
        chart: highchart
      };
    };

    $scope.drawImaxChart = function() {
      $scope.labelHighchart['imax'] = null;
      if ($scope.imax.refresh) {
        $interval.cancel($scope.imax.refresh);
      }
      $scope.imax.labels.some(function(label) {
        if (label.id == $scope.imax.id) {
          $scope.imax.label = $.extend({}, label);
          $scope.imax.label.id = 'imax';
          $scope.drawOneChart($scope.imax.label);
          return true;
        }
      });
      $scope.imax.refresh = $interval($scope.refreshChart, 10000);
    };

    $scope.refreshChart = function() {
      micheHttp.get('/product/price-latest').success(function(latest) {
        if (latest.datetime > $scope.imax.latestTime) {
          $scope.imax.latestTime = latest.datetime;
          var codePoint = {};
          $.each(latest.unitDataMap, function(code, val) {
            var prodLine = $scope.prodLineObj[code];
            var point = [latest.datetime, parseFloat(((val.price - prodLine.basePrice) / prodLine.basePrice * 100).toFixed(2))];
            prodLine.data.push(point);
            codePoint[code] = point;
          });
          $.each($scope.labelHighchart, function(key, val) {
            var chart = $('#chart-label-' + key).highcharts();
            chart.series.forEach(function(series, index) {
              var code = val.label.products[index].code;
              if (codePoint[code]) {
                series.addPoint(codePoint[code], false);
              }
            });
            chart.redraw();
          });
        }
      });
    };

    $scope.toTop = function(labelId) {
      if ($scope.imax.id != labelId) {
        $scope.imax.id = labelId;
        $scope.drawImaxChart();
      }
    };

    $scope.$on('$destroy', function() {
      if ($scope.imax.refresh) {
        $interval.cancel($scope.imax.refresh);
      }
    });

  }
]);

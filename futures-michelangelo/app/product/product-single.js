'use strict';

angular.module('miche.product.single', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/product/:code', {
    templateUrl: 'product/product-single.html',
    controller: 'micheProductSingleCtrl'
  });
}])

.controller('micheProductSingleCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {

  var preurl = 'http://139.196.37.92:8000/futures-api';
  var code = $routeParams.code;

  $.getJSON(preurl + '/web/futures/code/' + code, function(data) {
    var futures = data[0];

    $scope.base = {};
    $scope.base.code = futures.product.code;
    $scope.base.name = futures.product.name;

    $scope.price = {};
    $scope.price.price = futures.price.toLocaleString();
    $scope.price.priceTime = futures.priceTime;
    $scope.price.last1RInc = (futures.last1RIncPct * 100).toFixed(2);
    $scope.price.last5RInc = (futures.last5RIncPct * 100).toFixed(2);
    $scope.price.last10RInc = (futures.last10RIncPct * 100).toFixed(2);
    $scope.price.last30RInc = (futures.last30RIncPct * 100).toFixed(2);
    $scope.price.last60RInc = (futures.last60RIncPct * 100).toFixed(2);
    $scope.price.last1KInc = (futures.last1KIncPct * 100).toFixed(2);
    $scope.price.last5KInc = (futures.last5KIncPct * 100).toFixed(2);
    $scope.price.last10KInc = (futures.last10KIncPct * 100).toFixed(2);
    $scope.price.last30KInc = (futures.last30KIncPct * 100).toFixed(2);
    $scope.price.last60KInc = (futures.last60KIncPct * 100).toFixed(2);

    $scope.$digest();

    $.getJSON(preurl + '/web/price/realtime/' + code, function(realtime) {
      $('#realtimeChart').highcharts({
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
        legend: {
          enabled: false
        },
        tooltip: {
          shared: true,
          crosshairs: true
        },
        series: realtime
      });
    });

    $.getJSON(preurl + '/web/price/daily/' + code, function(daily) {
      $('#dailyChart').highcharts({
        chart: {
          zoomType: 'x'
        },
        title: {
          text: '日K价格走势'
        },
        subtitle: {
          text: futures.product.name + ' ' + futures.product.code
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
        series: daily
      });
    });
  });

}]);

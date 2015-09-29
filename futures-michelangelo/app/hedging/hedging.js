'use strict';

angular.module('miche.hedging', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/hedging', {
    templateUrl: 'hedging/hedging.html',
    controller: 'micheHedgingCtrl'
  });
}])

.controller('micheHedgingCtrl', ['$scope', '$http', 'CF',
  function($scope, $http, CF) {

    $scope.drawHedging = function(url, domId) {
      $.getJSON(url, function(data) {

        var series = data.prices.map(function(price) {
          return [price.d, price.p];
        });

        $('#' + domId).highcharts({
          chart: {
            zoomType: 'x'
          },
          title: {
            text: data.hedging.name + ' (' + data.hedging.downLimit + ',' + data.hedging.upLimit + ')'
          },
          xAxis: {
            type: "datetime"
          },
          yAxis: {
            title: {
              text: '钻石点'
            },
            plotBands: [{
              from: data.hedging.upLimit,
              to: data.hedging.upLimit + 10000,
              color: '#FFD700',
            }, {
              from: data.hedging.upLimit / 2,
              to: data.hedging.upLimit,
              color: '#FFF8DC',
            }, {
              from: data.hedging.downLimit,
              to: data.hedging.downLimit / 2,
              color: '#FFF8DC',
            }, {
              from: data.hedging.downLimit - 10000,
              to: data.hedging.downLimit,
              color: '#FFD700',
            }]
          },
          tooltip: {
            shared: true,
            crosshairs: true
          },
          legend: {
            enabled: false
          },
          series: [{
            name: 'hello',
            data: data.prices.map(function(price) {
              return [price.d, price.p];
            })
          }]

        });
      });

    };
    $scope.drawHedging(CF.preurl + '/hedging/realtime/1', 'container1');
    $scope.drawHedging(CF.preurl + '/hedging/daily/1', 'container2');
    $scope.drawHedging(CF.preurl + '/hedging/realtime/2', 'container3');
    $scope.drawHedging(CF.preurl + '/hedging/daily/2', 'container4');
    $scope.drawHedging(CF.preurl + '/hedging/realtime/3', 'container5');
    $scope.drawHedging(CF.preurl + '/hedging/daily/3', 'container6');
    $scope.drawHedging(CF.preurl + '/hedging/realtime/4', 'container7');
    $scope.drawHedging(CF.preurl + '/hedging/daily/4', 'container8');
  }
]);

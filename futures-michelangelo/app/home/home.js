'use strict';

angular.module('miche.home', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {
    templateUrl: 'home/home.html',
    controller: 'micheHomeCtrl'
  });
}])

.controller('micheHomeCtrl', ['$scope', '$http', 'CF',
  function($scope, $http, CF) {

    $http.get(CF.preurl + '/product/labels').success(function(labels) {
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
      $scope.prods = prodArr;
      $scope.labels = labels;
    });

    $http.get(CF.preurl + '/comments').success(function(comments) {
      $scope.comments = comments;
    });

    $scope.hasComments = function() {
      return $scope.comments && $scope.comments.length > 0;
    }

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
            },
            {
              from: data.hedging.upLimit / 2,
              to: data.hedging.upLimit,
              color: '#FFF8DC',
            },
            {
              from: data.hedging.downLimit,
              to: data.hedging.downLimit / 2,
              color: '#FFF8DC',
            },
             {
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
    $scope.drawHedging(CF.preurl + '/hedging/realtime/2', 'container2');
  }
]);

'use strict';

angular.module('miche.trend.prod', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/trend/prod/:code', {
    templateUrl: 'trend/trend-prod.html',
    controller: 'micheTrendProdCtrl'
  });
}])

.controller('micheTrendProdCtrl', ['$scope', '$routeParams', 'micheHttp', 'micheChart', 'micheData',
  function($scope, $routeParams, micheHttp, micheChart, micheData) {

    $scope.code = $routeParams.code;
    micheData.getProducts().then(function(prods) {
      $scope.prods = prods;
    });

    // define event handler
    $scope.dailyK = function(month) {
      micheHttp.get('/trend/prod-ma', {
        code: $scope.code,
        month: month
      }).success(function(prodma) {
        micheChart.drawTrendProd(prodma, 'trend-prod-chart');
      });
    };
    $scope.dailyK(3);

    $scope.dailyConK = function(contract, month) {
      micheHttp.get('/trend/contract-ma', {
        code: $scope.code,
        contract: contract,
        month: month
      }).success(function(prodma) {
        micheChart.drawTrendProd(prodma, 'trend-' + contract + '-chart')
      });
    };
    micheHttp.get('/product/prod-main-contract', {
      code: $scope.code
    }).success(function(contracts) {
      $scope.contracts = contracts;
      contracts.forEach(function(contract) {
        $scope.dailyConK(contract.month, 3);
      });
    });
  }
]);

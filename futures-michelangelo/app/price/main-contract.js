'use strict';

angular.module('miche.price.contract', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/price/contract/:code', {
    templateUrl: 'price/main-contract.html',
    controller: 'michePriceMainContractCtrl'
  });
}])

.controller('michePriceMainContractCtrl', ['$scope', '$routeParams', 'micheHttp', 'micheChart', 'micheData',
  function($scope, $routeParams, micheHttp, micheChart, micheData) {

    micheData.getProducts().then(function(prods) {
      $scope.prods = prods;
    });

    $scope.code = $routeParams.code;
    micheHttp.get('/product/prod-main-contract', {
      code: $scope.code
    }).success(function(contracts) {
      contracts.forEach(function(contract) {
          if(contract.month < 10) {
            contract.name = contract.code + '0' + contract.month;
          } else {
            contract.name = contract.code + contract.month;
          }
        });
      $scope.contracts = contracts;
    }).then(function() {
      micheHttp.get('/price/main-contract-year', {
        code: $scope.code
      }).success(function(containers) {
        containers.map(function(container) {
          micheChart.drawMainContractYear(container, 'line-years-' + container.name);
        });
      });
    });
  }
]);

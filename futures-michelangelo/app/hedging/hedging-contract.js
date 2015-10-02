'use strict';

angular.module('miche.hedging.contract', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/hedging/contract/:code', {
    templateUrl: 'hedging/hedging-contract.html',
    controller: 'micheHedgingContractCtrl'
  });
}])

.controller('micheHedgingContractCtrl', ['$scope', '$routeParams', 'micheHttp', 'micheChart', 'micheData',
  function($scope, $routeParams, micheHttp, micheChart, micheData) {

    micheData.getProducts().then(function(prods) {
      $scope.prods = prods;
    });

    $scope.code = $routeParams.code;
    micheHttp.get('/hedging/contracts-basic', {
      code: $scope.code
    }).success(function(contracts) {
      $scope.contracts = contracts;
    }).then(function() {
      micheHttp.get('/hedging/contracts', {
        code: $scope.code
      }).success(function(hedgings) {
        hedgings.map(function(hedging) {
          micheChart.drawHedgingContractYear(hedging, 'line-years-' + hedging.name);
          micheChart.drawHedgingContractVol(hedging, 'line-vol-' + hedging.name);
        });
      });
    });
  }
]);

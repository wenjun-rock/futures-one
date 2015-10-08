'use strict';

angular.module('miche.hedging.compare', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/hedging/compare', {
    templateUrl: 'hedging/hedging-compare.html',
    controller: 'micheHedgingCmpCtrl'
  });
}])

.controller('micheHedgingCmpCtrl', ['$scope', 'micheHttp', 'micheChart', 'micheData',
  function($scope, micheHttp, micheChart, micheData) {

    $scope.ctrl = {
      code1: 'A',
      code2: 'M'
    };

    micheData.getProducts().then(function(prods) {
      $scope.prods = prods;
    });

    $scope.compareProd = function() {
      if ($scope.ctrl.code1 == $scope.ctrl.code2) {
        return;
      }
      micheHttp.get('/hedging/prod-compare', {
        codes: $scope.ctrl.code1 + ',' + $scope.ctrl.code2
      }).success(function(seriesList) {
        micheChart.drawHedgingCompare(seriesList, 'hedgingCmpChart');
      });
    };
  }
]);

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
      code2: 'M',
      contract1: '',
      contract2: '',
    };

    micheData.getProducts().then(function(prods) {
      $scope.prods = prods;
    });

    $scope.compareProd = function() {
      if ($scope.ctrl.code1 == $scope.ctrl.code2) {
        return;
      }
      micheHttp.get('/hedging/prod-compare', {
        code1: $scope.ctrl.code1,
        code2: $scope.ctrl.code2
      }).success(function(seriesList) {
        micheChart.drawHedgingCompare(seriesList, 'hedgingCmpChart');
      }).error(function(data) {
        console.error(data);
        alert('出现错误！');
      });
    };

    $scope.compareContract = function() {
      if ($scope.ctrl.contract1 == '' || $scope.ctrl.contract2 == ''  || $scope.ctrl.contract1 == $scope.ctrl.contract2) {
        return;
      }
      micheHttp.get('/hedging/contract-compare', {
        contract1: $scope.ctrl.contract1,
        contract2: $scope.ctrl.contract2
      }).success(function(seriesList) {
        micheChart.drawHedgingCompare(seriesList, 'hedgingCmpChart');
      }).error(function(data) {
        console.error(data);
        alert('出现错误！');
      });
    };
  }
]);

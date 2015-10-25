'use strict';

angular.module('miche.home', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {
    templateUrl: 'home/home.html',
    controller: 'micheHomeCtrl'
  });
}])

.controller('micheHomeCtrl', ['$scope', 'micheHttp',
  function($scope, micheHttp) {

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
      $scope.prods = prodArr;
      $scope.labels = labels;
    });

    micheHttp.get('/comments').success(function(comments) {
      $scope.comments = comments;
    });

    $scope.hasComments = function() {
      return $scope.comments && $scope.comments.length > 0;
    }
    
  }
]);

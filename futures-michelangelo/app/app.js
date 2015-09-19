'use strict';

angular.module('micheApp', [
  'ngRoute',
  'miche.home',
  'miche.product.list',
  'miche.product.single'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({
    redirectTo: '/home'
  });
}]);

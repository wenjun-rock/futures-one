'use strict';

angular.module('micheApp', [
  'ngRoute',
  'ui.bootstrap',
  'miche.home',
  'miche.product.list',
  'miche.product.single'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({
    redirectTo: '/home'
  });
}])

.filter('percentage', function() {
  return function(input) {
    if (input) {
      return (input * 100).toFixed(2) + '%';
    } else {
      return '0.00%';
    }
  }
});

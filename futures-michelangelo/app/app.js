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

.constant('CF', {
  preurl: 'http://139.196.37.92:8000/futures-api'
})

.filter('percentage', function() {
  return function(input) {
    if (input) {
      return (input * 100).toFixed(2) + '%';
    } else {
      return '0.00%';
    }
  }
});

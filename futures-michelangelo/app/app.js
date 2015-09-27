'use strict';

angular.module('micheApp', [
  'ngRoute',
  'ui.bootstrap',
  'miche.home',
  'miche.label',
  'miche.product',
  'miche.label.wall',
  'miche.hedging'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({
    redirectTo: '/home'
  });
}])

.constant('CF', {
  preurl: 'http://f7p8.win:8000/futures-api',
  preurl2: 'http://localhost:8000/futures-api'
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

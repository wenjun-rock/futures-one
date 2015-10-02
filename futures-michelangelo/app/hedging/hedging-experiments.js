'use strict';

angular.module('miche.hedging.experiments', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/hedging/experiments', {
    templateUrl: 'hedging/hedging-experiments.html',
    controller: 'micheHedgingExpListCtrl'
  });
}])

.controller('micheHedgingExpListCtrl', ['$scope', 'micheHttp', 'micheChart', 'micheData',
  function($scope, micheHttp, micheChart, micheData) {

    micheHttp.get('/hedging/prod-experiments').success(function(experiments) {

      $('#experiment-table').dataTable({
        "data": experiments,
        "destroy": true,
        "info": false,
        "columns": [{
          "data": "name"
        }, {
          "data": "startDt"
        }, {
          "data": "endDt"
        }, {
          "data": "rsquared"
        }, {
          "data": "formula1"
        }, {
          "data": "stdError1"
        }, {
          "data": "formula2"
        }, {
          "data": "stdError2"
        }],
        "columnDefs": [{
          "render": function(data, type, row) {
            return '<a>' + data + '</a>';
          },
          "targets": [0]
        }]
      });
    });
  }
]);

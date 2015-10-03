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
            return '<a href="javascript:void(0)" onclick="$(\'#monitorId\').val(' + row.id + ');$(\'#monitorId\').click();return false;">' + data + '</a>';
          },
          "targets": [0]
        }]
      });
    });

    $scope.monitorExperiment = function() {
      var monitorId = $('#monitorId').val();
      console.log(monitorId);
      micheHttp.get('/hedging/prod-experiment', {
        id: monitorId
      }).success(function(experiment) {
        var hedging = {};

        hedging.name = experiment.name + " (" + experiment.formula1 + ") e=" + experiment.stdError1;
        hedging.upLimit = experiment.stdError1;
        hedging.downLimit = 0 - experiment.stdError1;
        hedging.data = experiment.prices1.map(function(price) {
            return [price.d, price.p];
        });
        micheChart.drawHedgingExperiment(hedging, 'monitor-experiment-1');

        hedging.name = experiment.name + " (" + experiment.formula2 + ") e=" + experiment.stdError2;
        hedging.upLimit = experiment.stdError2;
        hedging.downLimit = 0 - experiment.stdError2;
        hedging.data = experiment.prices2.map(function(price) {
            return [price.d, price.p];
        });
        micheChart.drawHedgingExperiment(hedging, 'monitor-experiment-2');
      });



    };

  }
]);

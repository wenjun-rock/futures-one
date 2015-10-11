'use strict';

angular.module('miche.hedging', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/hedging/prods', {
    templateUrl: 'hedging/hedging.html',
    controller: 'micheHedgingCtrl'
  });
}])

.controller('micheHedgingCtrl', ['$scope', 'micheHttp', 'micheChart', 'micheData',
  function($scope, micheHttp, micheChart, micheData) {

    micheHttp.get('/hedging/list').success(function(list) {
      $('#hedging-table').dataTable({
        "data": list,
        "destroy": true,
        "info": false,
        "order": [
          [2, "desc"]
        ],
        "columns": [{
          "data": "name"
        }, {
          "data": "formula"
        }, {
          "data": "complete"
        }, {
          "data": "diffRealtime"
        }, {
          "data": "diffKline"
        }, {
          "data": "down"
        }, {
          "data": "up"
        }, {
          "data": "mid"
        }, {
          "data": "q1"
        }, {
          "data": "q3"
        }],
        "columnDefs": [{
          "render": function(data, type, row) {
            return '<a href="javascript:void(0)" onclick="$(\'#monitorId\').val(' + row.id + ');$(\'#monitorId\').click();return false;">' + data + '</a>';
          },
          "targets": [0]
        }, {
          "render": function(data, type, row) {
            var out = (data * 100).toFixed(2);
            return out;
          },
          "targets": [2]
        }]
      });
    });

    $scope.monitorHedging = function() {
      var monitorId = $('#monitorId').val();
      micheHttp.get('/hedging/monitor', {
        id: monitorId
      }).success(function(hedgingMonitor) {
        var hedging = {
          down: hedgingMonitor.down,
          up: hedgingMonitor.up
        };

        hedging.name = hedgingMonitor.name + "(实时)";
        hedging.data = hedgingMonitor.realtimePrices.map(function(price) {
          return [price.d, price.p];
        });
        micheChart.drawHedging(hedging, 'monitor-hedging-realtime');

        hedging.name = hedgingMonitor.name + "(日K)";
        hedging.data = hedgingMonitor.klinePrices.map(function(price) {
          return [price.d, price.p];
        });
        micheChart.drawHedging(hedging, 'monitor-hedging-kline');
      });
    }
  }
]);

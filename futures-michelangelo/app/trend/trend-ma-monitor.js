'use strict';

angular.module('miche.trend.ma.monitor', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/trend/ma/monitor', {
    templateUrl: 'trend/trend-ma-monitor.html',
    controller: 'micheTrendMaMonitorCtrl'
  });
}])

.controller('micheTrendMaMonitorCtrl', ['$scope', 'micheHttp', 'micheChart',
  function($scope, micheHttp, micheChart) {

    micheHttp.get('/trend/monitor-trend-ma').success(function(monitorList) {
      $('#trend-ma-monitor-table').dataTable({
        "data": monitorList,
        "destroy": true,
        "info": false,
        "order": [
          [7, "asc"]
        ],
        "columns": [{
          "data": "code"
        }, {
          "data": "trend"
        }, {
          "data": "calDate"
        }, {
          "data": "prodPrice"
        }, {
          "data": "maPrice"
        }, {
          "data": "diff"
        }, {
          "data": "trendDays"
        }, {
          "data": "returnDays"
        }],
        "columnDefs": [{
          "render": function(data, type, row) {
            return '<a href="#/trend/prod/' + data + '" target="_blank" >' + data + '</a>';
          },
          "targets": [0]
        }, {
          "render": function(data, type, row) {
            if (data) {
              var date = new Date(data);
              var year = date.getFullYear();
              var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
              var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
              return year + "-" + month + "-" + day;
            } else {
              return '';
            }
          },
          "targets": [2]
        }, {
          "render": function(data, type, row) {
            return data.toLocaleString();
          },
          "targets": [3, 4]
        }, {
          "render": function(data, type, row) {
            var out = (data * 100).toFixed(2);
            if (out > 0) {
              return '<span style="color:red">' + out + '</span>';
            } else if (out < 0) {
              return '<span style="color:green">' + out + '</span>';
            } else {
              return out;
            }
          },
          "targets": [5]
        }, {
          "render": function(data, type, row) {
            if (data) {
              return data;
            } else {
              return 9999;
            }
          },
          "targets": [7]
        }]
      });
    });
  }
]);

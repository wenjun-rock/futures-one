'use strict';

angular.module('miche.hedging.prod.contract', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/hedging/prod/contract', {
    templateUrl: 'hedging/hedging-prod-contract.html',
    controller: 'micheHedgingProdContractCtrl'
  });
}])

.controller('micheHedgingProdContractCtrl', ['$scope', '$filter', '$modal', 'micheHttp', 'micheData',
  function($scope, $filter, $modal, micheHttp, micheData) {

    var allHedging;
    micheHttp.get('/hedging/list-hedging-contract').success(function(hedgings) {
      allHedging = hedgings.map(function(item){
        item.name = item.code1;
        item.name += item.month1 > 9 ? item.month1 : '0'+item.month1;
        item.name += ' - ';
        item.name += item.code2;
        item.name += item.month2 > 9 ? item.month2 : '0'+item.month2;
        return item;
      });

      $('#hedging-prod-contract-table').dataTable({
        "data": allHedging,
        "destroy": true,
        "info": false,
        "lengthMenu": [ 30, 50, 75, 100 ],
        "order": [[ 1, "desc" ]],
        "columns": [{
          "data": "name"
        }, {
          "data": "rate"
        }, {
          "data": "curr"
        }, {
          "data": "min"
        }, {
          "data": "down"
        }, {
          "data": "up"
        }, {
          "data": "max"
        }, {
          "data": "rank"
        }, {
          "data": "type"
        }, {
          "data": "refreshDt"
        }],
        "columnDefs": [{
          "render": function(data, type, row) {
            return '<a href="javascript:void(0)" onclick="$(\'#hedgingId\').val(' + row.id + ');$(\'#hedgingId\').click();return false;">' + data + '</a>';
          },
          "targets": [0]
        }, {
          "render": function(data, type, row) {
            return (data * 100).toFixed(2);
          },
          "targets": [1]
        }, {
          "render": function(data, type, row) {
            if(data) {
              return data.toLocaleString();
            } else {
              return null;
            }
          },
          "targets": [2,3,4,5,6]
        }, {
          "render": function(data, type, row) {
            return 'lv.' + data;
          },
          "targets": [7]
        }, {
          "render": function(data, type, row) {
            if(data == 1) {
              return '跨期';
            } else if(data == 2) {
              return '跨商品';
            } else {
              return null;
            }
          },
          "targets": [8]
        }, {
          "render": function(data, type, row) {
            return $filter('date')(data, 'yyyy-MM-dd');
          },
          "targets": [9]
        }]
      });
    });

    $scope.monitorHedgingContract = function() {
      var hedgingId = $('#hedgingId').val();
      var chosenHedging;
      allHedging.some(function(ele){
        if(ele.id == hedgingId) {
          chosenHedging = ele;
          return true;
        }
      });
      var modalInstance = $modal.open({
        size: 'lg',
        templateUrl: 'hedgingContractModal.html',
        controller: 'hedgingContractModalCtrl',
        resolve: {
          params: function() {
            return {
              hedging: chosenHedging
            };
          }
        }
      });
    };

  }
])

.controller('hedgingContractModalCtrl',
  function($scope, $filter, $q, micheHttp, micheData, micheChart, $modalInstance, params) {
    var chosenHedging = params.hedging;

    $scope.spot = function(range) {
      micheHttp.get('/hedging/get-hedging-contract-series', {
        'id': chosenHedging.id,
        'range': range
      }).success(function(series) {
        chosenHedging.data = series.prices.map(function(price) {
           return [price.d, price.p];
        });
        micheChart.drawHedging(chosenHedging, 'monitor-hedging-contract');
      });
    }

    $scope.spot(18);
  }
);

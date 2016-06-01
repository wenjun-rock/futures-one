'use strict';

angular.module('miche.trade.group.list', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/trade/groups', {
    templateUrl: 'trade/trade-group-list.html',
    controller: 'micheTradeGroupListCtrl'
  });
}])

.controller('micheTradeGroupListCtrl', ['$scope', '$filter', '$modal', 'micheHttp', 'micheChart', 'micheData',
  function($scope, $filter, $modal, micheHttp, micheChart, micheData) {

    var refresh = function() {
      micheHttp.get('/trade/list-trade-group').success(function(groupList) {
        $scope.groupList = groupList;

        $('#trade-group-table').dataTable({
          "data": groupList,
          "destroy": true,
          "info": false,
          "order": [
            [4, "desc"],
            [3, "desc"]
          ],
          "columns": [{
            "data": "name"
          }, {
            "data": "comment"
          }, {
            "data": "firstTradeDt"
          }, {
            "data": "lastTradeDt"
          }, {
            "data": "openVol"
          }, {
            "data": "fee"
          }, {
            "data": "profit"
          }, {
            "data": "amount"
          }, {
            "data": "vol"
          }, {
            "data": "id"
          }],
          "columnDefs": [{
            "render": function(data, type, row) {
              return row.name + ' (' + row.id + ')';
            },
            "targets": [0]
          }, {
            "render": function(data, type, row) {
              return $filter('date')(data, 'yyyy-MM-dd HH:mm:ss');
            },
            "targets": [2,3]
          }, {
            "render": function(data, type, row) {
              if(data) {
                return data.toLocaleString();
              } else {
                return null;
              }
            },
            "targets": [4,5,6,7,8]
          }, {
            "render": function(data, type, row) {
              return '<button type="button" class="btn btn-info trade-btn" onclick="$(\'#detailId\').val(' + row.id + ');$(\'#detailId\').click();">详细</button>';
            },
            "targets": [9]
          }]
        });
      });
    };

    $scope.detailTradeGroupModal = function() {
      var modalInstance = $modal.open({
        templateUrl: 'detailTradeGroupModal.html',
        controller: 'micheDetailTradeGroupModalCtrl'
      });
      modalInstance.result.then(function() {
        refresh();
      });
    };
    $scope.detailTradeGroupModal = function() {
      var detailId = $('#detailId').val();
      var detailGroup;
      $scope.groupList.some(function(trade) {
        if (trade.id == detailId) {
          detailGroup = trade;
          return true;
        }
      });
      var modalInstance = $modal.open({
        size: 'lg',
        templateUrl: 'detailTradeGroupModal.html',
        controller: 'micheDetailTradeGroupModalCtrl',
        resolve: {
          params: function() {
            return {
              'group': detailGroup
            };
          }
        }
      });
    };

    refresh();

  }
])

.controller('micheDetailTradeGroupModalCtrl',
  function($scope, $filter, $q, micheHttp, micheData, $modalInstance, params) {
    $scope.group = params.group;

    var deferred = $q.defer();
    deferred.promise.then(function(){
      console.log(params.group);
      $('#elmts-table').dataTable({
        "data": params.group.elmts,
        "destroy": true,
        "paging": false,
        "searching": false,
        "info": false,
        "order": [
            [3, "desc"],
            [2, "desc"]
          ],
        "columns": [{
          "data": "conCode"
        }, {
          "data": "type"
        }, {
          "data": "openDt"
        }, {
          "data": "closeDt"
        }, {
          "data": "openPrice"
        }, {
          "data": "closePrice"
        }, {
          "data": "diffPrice"
        }],
        "columnDefs": [{
          "render": function(data, type, row) {
            return $filter('date')(data, 'yyyy-MM-dd HH:mm:ss');
          },
          "targets": [2,3]
        }, {
          "render": function(data, type, row) {
            if (data) {
              return data.toLocaleString();
            } else {
              return '';
            }
          },
          "targets": [4,5,6]
        }, {
         "render": function(data, type, row) {
            if (data == 1) {
              return '多'
            } else if (data == 2) {
              return '空'
            }
          },
          "targets": [1]
        }]
      });
    });
    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };
    setTimeout(function() {  
      deferred.resolve('');
    }, 100);
      
  }
);

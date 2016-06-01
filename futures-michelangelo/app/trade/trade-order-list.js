'use strict';

angular.module('miche.trade.order.list', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/trade/orders', {
    templateUrl: 'trade/trade-order-list.html',
    controller: 'micheTradeOrderListCtrl'
  });
}])

.controller('micheTradeOrderListCtrl', ['$scope', '$filter', '$modal', 'micheHttp', 'micheChart', 'micheData',
  function($scope, $filter, $modal, micheHttp, micheChart, micheData) {

    var refresh = function() {
      micheHttp.get('/trade/list-trade-order').success(function(orderList) {
        $scope.orderList = orderList;
        $scope.totalAmount = 0;
        $scope.totalFee = 0;
        $scope.totalProfit = 0;
        orderList.forEach(function(order) {
          $scope.totalAmount += order.amount | 0;
          $scope.totalFee += order.fee | 0;
          $scope.totalProfit += order.profit | 0;
        });

        $('#trade-order-table').dataTable({
          "data": orderList,
          "destroy": true,
          "info": false,
          "order": [
          [1, "desc"]
          ],
          "columns": [{
            "data": "conCode"
          }, {
            "data": "tradeDt"
          }, {
            "data": "type"
          }, {
            "data": "price"
          }, {
            "data": "vol"
          }, {
            "data": "amount"
          }, {
            "data": "fee"
          }, {
            "data": "profit"
          }, {
            "data": "comment"
          }, {
            "data": "groupId"
          }, {
            "data": "id"
          }],
          "columnDefs": [{
            "render": function(data, type, row) {
              return $filter('date')(data, 'yyyy-MM-dd HH:mm:ss');
            },
            "targets": [1]
          }, {
            "render": function(data, type, row) {
              if (data == 1) {
                return '买开'
              } else if (data == 2) {
                return '卖开'
              } else if (data == 3) {
                return '卖平'
              } else if (data == 4) {
                return '买平'
              } else {
                return ''
              }
            },
            "targets": [2]
          }, {
            "render": function(data, type, row) {
              if(data) {
                return data.toLocaleString();
              } else {
                return null;
              }
            },
            "targets": [3,4,5,6,7]
          }, {
            "render": function(data, type, row) {
              if(data) {
                return row.groupName + ' (' + row.groupId + ')';
              } else {
                return ' <button type="button" class="btn btn-info trade-btn" onclick="$(\'#assignId\').val(' + row.id + ');$(\'#assignId\').click();">分组</button>';
              }
            },
            "targets": [9]
          }]
        });
      });
  };

  $scope.addTradeOrderModal = function() {
    var modalInstance = $modal.open({
      templateUrl: 'addTradeOrderModal.html',
      controller: 'micheAddTradeOrderModalCtrl'
    });
    modalInstance.result.then(function() {
      refresh();
    });
  };

  $scope.assignTradeOrderModal = function() {
    var modalInstance = $modal.open({
      templateUrl: 'assignTradeOrderModal.html',
      controller: 'micheAssignTradeOrderModalCtrl'
    });
    modalInstance.result.then(function() {
      refresh();
    });
  };

  refresh();
}])
.controller('micheAssignTradeOrderModalCtrl',
  function($scope, micheHttp, micheData, $modalInstance) {

    $scope.groups = [];
    micheHttp.get('/trade/list-trade-group', {
      "order":false
    }).success(function(trade) {
      trade.forEach(function(ele) {
        ele.labelName = ele.name + ' (' + ele.id + ')';
      });
      $scope.groups = trade;
    });

    $scope.ok = function() {
      var orderId = $('#assignId').val();
      micheHttp.post('/trade/assign-trade-order', {
        'groupId':$scope.group,
        'orderId':$('#assignId').val()
      }).success(function() {
        $modalInstance.close();
      });
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

  }
  )
.controller('micheAddTradeOrderModalCtrl',
  function($scope, micheHttp, micheData, $modalInstance) {

    $scope.ok = function() {
      var fd = new FormData();
      fd.append('file', $scope.tradeFile);
      micheHttp.post('/trade/upload-trade-order', fd, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
      }).success(function(trade) {
        if (trade) {
          $modalInstance.close(trade);
        } else {
          alert('Error!');
        }
      });
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

  }
  )
.directive('fileModel', ['$parse', function ($parse) {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
      var model = $parse(attrs.fileModel);
      var modelSetter = model.assign;

      element.bind('change', function(){
        scope.$apply(function(){
          modelSetter(scope, element[0].files[0]);
        });
      });
    }
  };
}]);

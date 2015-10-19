'use strict';

angular.module('miche.trade.list', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/trade', {
    templateUrl: 'trade/trade-list.html',
    controller: 'micheTradeListCtrl'
  });
}])

.controller('micheTradeListCtrl', ['$scope', '$modal', 'micheHttp', 'micheChart', 'micheData',
  function($scope, $modal, micheHttp, micheChart, micheData) {

    var refresh = function() {
      micheHttp.get('/trade/list-trade').success(function(tradeList) {
        $scope.tradeList = tradeList;
        $scope.totalFloatProfit = 0;
        $scope.totalCompleteProfit = 0;
        $scope.totalProfit = 0;
        tradeList.forEach(function(trade) {
          $scope.totalFloatProfit += trade.floatProfit;
          $scope.totalCompleteProfit += trade.completeProfit;
          $scope.totalProfit += trade.profit;
        });

        $('#trade-table').dataTable({
          "data": tradeList,
          "destroy": true,
          "info": false,
          "order": [
            [4, "desc"],
            [2, "desc"]
          ],
          "columns": [{
            "data": "name"
          }, {
            "data": "type"
          }, {
            "data": "startDt"
          }, {
            "data": "endDt"
          }, {
            "data": "vol"
          }, {
            "data": "margin"
          }, {
            "data": "floatProfit"
          }, {
            "data": "completeProfit"
          }, {
            "data": "profit"
          }, {
            "data": "maxMargin"
          }, {
            "data": "roi"
          }, {
            "data": "id"
          }],
          "columnDefs": [{
            "render": function(data, type, row) {
              if (data == 1) {
                return '开多'
              } else if (data == 2) {
                return '开空'
              } else if (data == 3) {
                return '对冲'
              }
            },
            "targets": [1]
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
            "targets": [2, 3]
          }, {
            "render": function(data, type, row) {
              return data.toLocaleString();
            },
            "targets": [5, 9]
          }, {
            "render": function(data, type, row) {
              if (data > 0) {
                return '<span style="color:red">' + data.toLocaleString() + '</span>';
              } else if (data < 0) {
                return '<span style="color:green">' + data.toLocaleString() + '</span>';
              } else {
                return data;
              }
            },
            "targets": [6, 7, 8]
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
            "targets": [10]
          }, {
            "orderable": false,
            "render": function(data, type, row) {
              var ele = '<button type="button" class="btn btn-info trade-btn" onclick="$(\'#detailId\').val(' + row.id + ');$(\'#detailId\').click();">详情</button>';
              if (row.endDt == null) {
                ele = ele + '<button type="button" class="btn btn-warning trade-btn" onclick="$(\'#openId\').val(' + row.id + ');$(\'#openId\').click();">开仓</button>' //
                  + '<button type="button" class="btn btn-danger trade-btn" onclick="$(\'#closeId\').val(' + row.id + ');$(\'#closeId\').click();">平仓</button>';
              }
              return ele;
            },
            "targets": [11]
          }]
        });
      });
    };

    $scope.addTradeModal = function() {
      var modalInstance = $modal.open({
        templateUrl: 'addTradeModal.html',
        controller: 'micheAddTradeModalCtrl'
      });
      modalInstance.result.then(function() {
        refresh();
      });
    };
    $scope.openPositionModal = function() {
      var openId = $('#openId').val();
      var openTrade;
      $scope.tradeList.some(function(trade) {
        if (trade.id == openId) {
          openTrade = trade;
          return true;
        }
      });
      var modalInstance = $modal.open({
        templateUrl: 'openPositionModal.html',
        controller: 'micheOpenPositionModalCtrl',
        resolve: {
          params: function() {
            return {
              trade: openTrade
            };
          }
        }
      });
      modalInstance.result.then(function() {
        refresh();
      });
    };
    $scope.detailTradeModal = function() {
      var detailId = $('#detailId').val();
      var detailTrade;
      $scope.tradeList.some(function(trade) {
        if (trade.id == detailId) {
          detailTrade = trade;
          return true;
        }
      });
      var modalInstance = $modal.open({
        size: 'lg',
        templateUrl: 'detailTradeModal.html',
        controller: 'micheDetailTradeModalCtrl',
        resolve: {
          params: function() {
            return {
              trade: detailTrade
            };
          }
        }
      });
      modalInstance.result.then(function() {
        refresh();
      });
    };

    refresh();

  }
])

.controller('micheAddTradeModalCtrl',
  function($scope, micheHttp, micheData, $modalInstance) {
    $scope.tradeTypes = [{
      code: '1',
      name: '开多'
    }, {
      code: '2',
      name: '开空'
    }, {
      code: '3',
      name: '对冲'
    }];
    $scope.trade = {
      name: '',
      type: '3'
    };

    $scope.ok = function() {
      micheHttp.post('/trade/add-trade', $scope.trade)
        .success(function(trade) {
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

.controller('micheDetailTradeModalCtrl',
  function($scope, micheHttp, micheData, $modalInstance, params) {
    $scope.trade = params.trade;
    micheHttp.get('/trade/detail-trade', {
      id: params.trade.id
    }).success(function(detail) {
      $('#balance-table').dataTable({
        "data": detail.balanceList,
        "destroy": true,
        "paging": false,
        "searching": false,
        "info": false,
        "columns": [{
          "data": "conCode"
        }, {
          "data": "type"
        }, {
          "data": "vol"
        }, {
          "data": "avgCostPrice"
        }, {
          "data": "price"
        }, {
          "data": "margin"
        }, {
          "data": "floatProfit"
        }, {
          "data": "completeProfit"
        }, {
          "data": "profit"
        }],
        "columnDefs": [{
          "render": function(data, type, row) {
            if (data == 1) {
              return '多'
            } else if (data == 2) {
              return '空'
            }
          },
          "targets": [1]
        }, {
          "render": function(data, type, row) {
            if (data) {
              return data.toLocaleString();
            } else {
              return '';
            }
          },
          "targets": [3, 4, 5]
        }, {
          "render": function(data, type, row) {
            if (data > 0) {
              return '<span style="color:red">' + data.toLocaleString() + '</span>';
            } else if (data < 0) {
              return '<span style="color:green">' + data.toLocaleString() + '</span>';
            } else {
              return data;
            }
          },
          "targets": [6, 7, 8]
        }]
      });

      $('#action-table').dataTable({
        "data": detail.actionList,
        "destroy": true,
        "paging": false,
        "searching": false,
        "info": false,
        "columns": [{
          "data": "id"
        }, {
          "data": "dt"
        }, {
          "data": "conCode"
        }, {
          "data": "type"
        }, {
          "data": "vol"
        }, {
          "data": "price"
        }],
        "columnDefs": [{
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
          "targets": [1]
        }, {
          "render": function(data, type, row) {
            if (data) {
              return data.toLocaleString();
            } else {
              return '';
            }
          },
          "targets": [5]
        }, {
         "render": function(data, type, row) {
            if (data == 1) {
              return '多'
            } else if (data == 2) {
              return '空'
            }
          },
          "targets": [3]
        }]
      });
    });
  }
)

.controller('micheOpenPositionModalCtrl',
  function($scope, micheHttp, micheData, $modalInstance, params) {
    $scope.actionTypes = [{
      code: '1',
      name: '开多'
    }, {
      code: '2',
      name: '开空'
    }];
    $scope.trade = params.trade;
    $scope.action = {
      tradeId: params.trade.id,
      conCode: '',
      dt: new Date(),
      type: '1',
      price: '',
      vol: ''
    };

    $scope.ok = function() {
      micheHttp.post('/trade/add-action', $scope.action)
        .success(function(action) {
          if (action) {
            $modalInstance.close(action);
          } else {
            alert('Error!');
          }
        });
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

  }
);

'use strict';

angular.module('miche.product.single', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/product/code/:code', {
    templateUrl: 'product/product-single.html',
    controller: 'micheProductSingleCtrl'
  });
}])

.controller('micheProductSingleCtrl', ['$scope', '$routeParams', '$http', '$location', '$modal',
  function($scope, $routeParams, $http, $location, $modal) {

    var preurl = 'http://139.196.37.92:8000/futures-api';

    $http.get(preurl + '/product/labels').success(function(labels) {
      $scope.labelMenu = {
        labels: labels,
        closeOthers: true
      };
    });

    $scope.selectCode = function(code) {
      $scope.product = {};
      $http.get(preurl + '/product/info/' + code).success(function(info) {
        $scope.product.info = info;
      });
      $http.get(preurl + '/product/price/code/' + code).success(function(price) {
        $scope.product.price = price;
      });
      $http.get(preurl + '/comments?type=1&key=' + code).success(function(comments) {
        $scope.product.comments = comments;
      });

      $.getJSON(preurl + '/price/realtime?codes=' + code, function(realtime) {
        $('#realtimeChart').highcharts({
          chart: {
            zoomType: 'x'
          },
          title: {
            text: '实时价格走势'
          },
          xAxis: {
            type: "datetime"
          },
          yAxis: {
            title: {
              text: '价格'
            }
          },
          legend: {
            enabled: false
          },
          tooltip: {
            shared: true,
            crosshairs: true
          },
          series: realtime
        });
      });

      $.getJSON(preurl + '/price/daily?codes=' + code, function(daily) {
        $('#dailyChart').highcharts({
          chart: {
            zoomType: 'x'
          },
          title: {
            text: '日K价格走势'
          },
          xAxis: {
            type: "datetime"
          },
          yAxis: {
            title: {
              text: '价格'
            }
          },
          legend: {
            enabled: false
          },
          tooltip: {
            shared: true,
            crosshairs: true
          },
          series: daily
        });
      });
    };

    $scope.selectCode($routeParams.code);

    $scope.hasComments = function() {
      return $scope.product.comments && $scope.product.comments.length > 0;
    }

    $scope.toLabel = function(labelId) {
      $location.path('/product/label/' + labelId);
    }

    $scope.openModal = function() {
      var modalInstance = $modal.open({
        templateUrl: 'myModalComment.html',
        controller: 'micheProductSingleModalCtrl',
        resolve: {
          params: function() {
            return {
              type: 1,
              key: $scope.product.info.code
            };
          }
        }
      });

      modalInstance.result.then(function(selectedItem) {
        $scope.selected = selectedItem;
      });
    }

  }
])

.controller('micheProductSingleModalCtrl',
  function($scope, $http, $modalInstance, params) {

    $scope.params = params;

    $scope.ok = function() {
      $modalInstance.close($scope.selected.item);
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };
  });

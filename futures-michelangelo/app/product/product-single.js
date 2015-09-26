'use strict';

angular.module('miche.product.single', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/product/code/:code', {
    templateUrl: 'product/product-single.html',
    controller: 'micheProductSingleCtrl'
  });
}])

.controller('micheProductSingleCtrl', ['$scope', '$routeParams', '$http', '$location', '$modal', 'CF',

  function($scope, $routeParams, $http, $location, $modal, CF) {

    var preurl = CF.preurl;

    $http.get(preurl + '/product/labels').success(function(labels) {
      $scope.labelMenu = {
        labels: labels,
        closeOthers: true
      };
    });

    $scope.selectCode = function(code) {
      $scope.product = {};
      $http.get(preurl + '/product/prod-info?code=' + code).success(function(info) {
        $scope.product.info = info;
      });
      $http.get(preurl + '/product/price-prod-aggre?code=' + code).success(function(price) {
        $scope.product.price = price;
      });
      $http.get(preurl + '/comments?type=1&key=' + code).success(function(comments) {
        $scope.product.comments = comments;
      });

      $.getJSON(preurl + '/product/price-prod-realtime?codes=' + code, function(realtime) {
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

      $.getJSON(preurl + '/product/price-prod-daily?codes=' + code, function(daily) {
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
    };

    $scope.toLabel = function(labelId) {
      $location.path('/product/label/' + labelId);
    };

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
      modalInstance.result.then(function(comment) {
        $scope.product.comments.unshift(comment);
      });
    };

  }
])

.controller('micheProductSingleModalCtrl',
  function($scope, $http, CF, $modalInstance, params) {

    $scope.params = params;
    $scope.content = '';

    $scope.ok = function() {
      $http.post(CF.preurl + '/comments', {
        type: $scope.params.type,
        key: $scope.params.key,
        content: $scope.content
      }).success(function(comment) {
        if (comment) {
          $modalInstance.close(comment);
        } else {
          alert('Error!');
        }
      });
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

  });

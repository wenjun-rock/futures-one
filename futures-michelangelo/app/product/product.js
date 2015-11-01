'use strict';

angular.module('miche.product', ['ngRoute', 'miche.services'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/product/code/:code', {
    templateUrl: 'product/product.html',
    controller: 'micheProductCtrl'
  });
}])

.controller('micheProductCtrl', ['$scope', '$routeParams', '$location', '$modal', 'micheHttp', 'micheChart', 'micheData',
  function($scope, $routeParams, $location, $modal, micheHttp, micheChart, micheData) {

    micheData.getLabels(true).then(function(labels) {
      labels.some(function(label) {
        if (label.id == Number($routeParams.from)) {
          label.open = true;
          return true;
        }
      });
      $scope.labelMenu = {
        labels: labels,
        closeOthers: true
      };
    });

    var code = $routeParams.code

    $scope.product = {};
    micheHttp.get('/product/prod-info', {
      code: code
    }).success(function(info) {
      $scope.product.info = info;
    });
    micheHttp.get('/product/price-prod-aggre', {
      code: code
    }).success(function(price) {
      $scope.product.price = price;
    });
    micheHttp.get('/comments', {
      type: 1,
      key: code
    }).success(function(comments) {
      $scope.product.comments = comments;
    });

    micheHttp.get('/product/price-prod-realtime', {
      codes: code
    }).success(function(realtime) {
      micheChart.drawProdRealtimeChart(realtime, 'realtimeChart');
    });

    micheHttp.get('/product/price-contract-daily', {
      code: code
    }).success(function(prodContracts) {
      $scope.contract = {
        priceSeries: [],
        volCategories: [],
        volDataLatest: [],
        volDataTotal: [],
        maxLatestVol: 0,
        maxTotalVol: 0,
        mainContract: []
      };
      prodContracts.contracts.forEach(function(contract) {
        $scope.contract.volCategories.push(contract.contract);
        $scope.contract.volDataLatest.push(contract.latestVol);
        $scope.contract.volDataTotal.push(contract.totalVol);
        $scope.contract.maxLatestVol = Math.max($scope.contract.maxLatestVol, contract.latestVol);
        $scope.contract.maxTotalVol = Math.max($scope.contract.maxTotalVol, contract.totalVol);
        $scope.contract.priceSeries.push({
          name: contract.contract,
          data: contract.prices.map(function(price) {
            return [price.d, price.p];
          })
        });
      });
      var mainMonth = {};
      prodContracts.contracts.forEach(function(contract, index) {
        if ($scope.contract.volDataTotal[index] > $scope.contract.maxTotalVol * 0.05) {
          $scope.contract.mainContract.push(true);
          mainMonth[contract.contract.substr(-2)] = true;
        } else if (mainMonth[contract.contract.substr(-2)]) {
          $scope.contract.mainContract.push(true);
        } else {
          $scope.contract.mainContract.push(false);
        }
      });

      $('#contractPriceChart').highcharts({
        chart: {
          zoomType: 'x'
        },
        title: {
          text: '合约价格'
        },
        xAxis: {
          type: "datetime",
          dateTimeLabelFormats: {
            millisecond: '%H:%M:%S.%L',
            second: '%H:%M:%S',
            minute: '%H:%M',
            hour: '%H:%M',
            day: '%m-%d',
            week: '%m-%d',
            month: '%Y-%m'
          }
        },
        yAxis: {
          title: {
            text: '价格'
          }
        },
        tooltip: {
          shared: true,
          crosshairs: true
        },
        series: $scope.contract.priceSeries
      });
      $('#contractVolChart').highcharts({
        chart: {
          type: 'column',
          zoomType: 'x'
        },
        title: {
          text: prodContracts.latest + '成交量统计'
        },
        xAxis: {
          categories: $scope.contract.volCategories,
          crosshair: true
        },
        yAxis: [{
          min: 0,
          title: {
            text: '当日成交量'
          }
        }, {
          title: {
            text: '累积成交量'
          },
          opposite: true
        }],
        tooltip: {
          shared: true,
          crosshairs: true
        },
        colors: ['#7cb5ec', '#90ed7d'],
        series: [{
          name: '当日',
          type: 'spline',
          yAxis: 0,
          data: $scope.contract.volDataLatest
        }, {
          name: '累积',
          type: 'column',
          yAxis: 1,
          data: $scope.contract.volDataTotal
        }]
      });
    });

    // define event handler
    $scope.dailyK = function(month) {
      micheHttp.get('/product/price-prod-daily', {
        code: code,
        month: month
      }).success(function(daily) {
        micheChart.drawProdDailyPrice(daily, 'dailyChart');
      });
    };
    $scope.dailyK(3);

    // define event handler
    $scope.spot = function(month) {
      micheHttp.get('/product/price-prod-spot', {
        code: code,
        month: month
      }).success(function(spot) {
        micheChart.drawProdSpotPrice(spot, 'spotChart');
      });
    };
    $scope.spot(3);

    $scope.showAllContract = function() {
      $('#contractPriceChart').highcharts().series.forEach(function(series, index) {
        if (!series.visible) {
          series.show();
        }
      });
    };

    $scope.showMainContract = function() {
      $('#contractPriceChart').highcharts().series.forEach(function(series, index) {
        if (series.visible && !$scope.contract.mainContract[index]) {
          series.hide();
        } else if (!series.visible && $scope.contract.mainContract[index]) {
          series.show();
        }
      });
    };

    $scope.hasComments = function() {
      return $scope.product.comments && $scope.product.comments.length > 0;
    };


    $scope.hasComments = function() {
      return $scope.product.comments && $scope.product.comments.length > 0;
    };

    $scope.toLabel = function(labelId) {
      $location.path('/product/label/' + labelId);
    };

    $scope.toCode = function(code, labelId) {
      $location.path('/product/code/' + code).search({
        from: labelId
      });
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

  }
);

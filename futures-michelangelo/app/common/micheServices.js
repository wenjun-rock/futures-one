'use strict';

angular.module('miche.services', [])

.factory('micheHttp', ['$http', 'CF',
  function($http, CF) {
    var service = {};

    service.get = function(url, params) {
      var targetUrl = CF.preurl + url;
      if (params) {
        var paramstr = '';
        $.each(params, function(key, val) {
          paramstr += '&' + key + '=' + val;
        });
        targetUrl += '?' + paramstr.substr(1);
      }
      console.log(targetUrl);
      return $http.get(targetUrl);
    };

    return service;
  }
])

.factory('micheChart', ['$http', 'CF',
  function($http, CF) {

    var defaultOptions = {
      credits: {
        enabled: false
      },
      chart: {
        zoomType: 'x'
      },
      tooltip: {
        shared: true,
        crosshairs: true,
        dateTimeLabelFormats: {
          day: '%Y-%m-%d'
        }
      },
      xAxis: {
        type: "datetime",
        dateTimeLabelFormats: {
          day: '%m-%d',
          week: '%m-%d',
          month: '%Y-%m',
          Year: '%Y'
        }
      }
    };

    var service = {};

    service.drawHedgingContractYear = function(hedging, domId) {
      var yearSeries = hedging.yearLines.map(function(ele) {
        return {
          name: ele.year,
          data: ele.line.map(function(wrap) {
            return [wrap.dt, wrap.p.d];
          })
        };
      });
      $('#' + domId).highcharts($.extend(true, {}, defaultOptions, {
        title: {
          text: hedging.name + '价差同比'
        },
        xAxis: {
          dateTimeLabelFormats: {
            month: '%m'
          }
        },
        yAxis: {
          title: {
            text: '价差'
          }
        },
        tooltip: {
          dateTimeLabelFormats: {
            day: '%m-%d'
          }
        },
        series: yearSeries
      }));
    };

    service.drawHedgingContractVol = function(hedging, domId) {
      var names = hedging.name.split('-');
      var series = [{
        type: 'spline',
        zIndex: 10,
        yAxis: 0,
        name: hedging.name,
        data: []
      }, {
        type: 'area',
        zIndex: 1,
        yAxis: 1,
        name: names[0],
        data: []
      }, {
        type: 'area',
        zIndex: 2,
        yAxis: 1,
        name: names[1],
        data: []
      }];
      hedging.line.forEach(function(ele) {
        series[0].data.push([ele.dt, ele.d]);
        series[1].data.push([ele.dt, ele.vol1]);
        series[2].data.push([ele.dt, ele.vol2]);
      });
      $('#' + domId).highcharts($.extend(true, {}, defaultOptions, {
        title: {
          text: hedging.name + '价差走势'
        },
        yAxis: [{
          title: {
            text: '价差'
          }
        }, {
          title: {
            text: '成交量'
          },
          opposite: true
        }],
        series: series
      }));
    };

    service.drawHedgingExperiment = function(hedging, domId) {
      $('#' + domId).highcharts({
        chart: {
          zoomType: 'x'
        },
        title: {
          text: hedging.name
        },
        xAxis: {
          type: "datetime",
          plotBands: [{
            from: hedging.startDt,
            to: hedging.endDt,
            color: 'rgba(68, 170, 213, .2)'
          }]
        },
        yAxis: {
          title: {
            text: '钻石点'
          },
          plotBands: [{
            from: hedging.upLimit,
            to: hedging.upLimit + 10000,
            color: '#FFD700',
          }, {
            from: hedging.downLimit - 10000,
            to: hedging.downLimit,
            color: '#FFD700',
          }]
        },
        tooltip: {
          crosshairs: true,
          dateTimeLabelFormats: {
            day: '%Y-%m-%d'
          }
        },
        legend: {
          enabled: false
        },
        series: [{
          name: 'diff',
          data: hedging.data
        }]

      });

    };



    return service;
  }
])

.factory('micheData', ['$q', 'micheHttp',
  function($q, micheHttp) {
    var prods, labels, labelsIncludeAll;

    var getLabels = function() {
      return micheHttp.get('/product/labels').success(function(res) {
        labels = res;
        var prodArr = [],
          prodTmp = {};
        labels.forEach(function(label) {
          label.products.forEach(function(prod) {
            if (!prodTmp[prod.code]) {
              prodTmp[prod.code] = true;
              prodArr.push(prod);
            }
          });
        });
        prods = prodArr;
        labelsIncludeAll = [{
          id: 0,
          name: '全部',
          products: prodArr
        }].concat(labels);
      });
    };

    var service = {};

    service.getProducts = function() {
      if (prods) {
        return $q.when(prods);
      } else {
        return getLabels().then(function() {
          return prods;
        });
      }
    };

    service.getLabels = function() {
      if (labels) {
        return $q.when(labels);
      } else {
        return getLabels().then(function() {
          return labels;
        });
      }
    };

    service.getLabelsIncludeAll = function() {
      if (labelsIncludeAll) {
        return $q.when(labelsIncludeAll);
      } else {
        return getLabels().then(function() {
          return labelsIncludeAll;
        });
      }
    };

    return service;
  }
]);

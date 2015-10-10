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

    service.drawHedging = function(hedging, domId) {
      $('#' + domId).highcharts($.extend(true, {}, defaultOptions, {
        title: {
          text: hedging.name
        },
        yAxis: {
          title: {
            text: '价差'
          },
          plotBands: [{
            from: hedging.up,
            to: hedging.up + 10000,
            color: '#FFD700',
          }, {
            from: hedging.down - 10000,
            to: hedging.down,
            color: '#FFD700',
          }]
        },
        legend: {
          enabled: false
        },
        series: [{
          name: 'diff',
          data: hedging.data
        }]
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
            color: '#FF8C69'
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

    service.drawHedgingCompare = function(seriesList, domId) {
      var series = [{
        yAxis: 0,
        zIndex: 2,
        name: seriesList[0].name,
        data: seriesList[0].prices.map(function(ele) {
          return [ele.d, ele.p];
        })
      }, {
        yAxis: 0,
        zIndex: 2,
        name: seriesList[1].name,
        data: seriesList[1].prices.map(function(ele) {
          return [ele.d, ele.p];
        })
      }, {
        yAxis: 1,
        zIndex: 1,
        type: 'column',
        name: seriesList[2].name,
        data: seriesList[2].prices.map(function(ele) {
          return [ele.d, ele.p];
        })
      }];

      $('#' + domId).highcharts($.extend(true, {}, defaultOptions, {
        title: {
          text: seriesList[2].name
        },
        colors: ['#2f7ed8', '#492970', '#8bbc21'],
        yAxis: [{
          title: {
            text: '价格'
          }
        }, {
          title: {
            text: '价差'
          },
          opposite: true
        }],
        series: series
      }));
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

'use strict';

angular.module('miche.home', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {
    templateUrl: 'home/home.html',
    controller: 'micheHomeCtrl'
  });
}])

.controller('micheHomeCtrl', [function() {

  $.getJSON('http://139.196.37.92:8000/futures-api/web/hedging/realtime/1', function(data) {
    $('#container1').highcharts({
      chart: {
        zoomType: 'x'
      },
      title: {
        text: 'USD to EUR exchange rate over time'
      },
      subtitle: {
        text: document.ontouchstart === undefined ?
          'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
      },
      xAxis: {
        type: "datetime"
      },
      yAxis: {
        title: {
          text: 'Exchange rate'
        }
      },
      tooltip: {
        shared: true,
        crosshairs: true
      },
      series: data
    });
  });

  $.getJSON('http://139.196.37.92:8000/futures-api/web/hedging/realtime/2', function(data) {
    $('#container2').highcharts({
      chart: {
        zoomType: 'x'
      },
      title: {
        text: 'USD to EUR exchange rate over time'
      },
      subtitle: {
        text: document.ontouchstart === undefined ?
          'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
      },
      xAxis: {
        type: "datetime"
      },
      yAxis: {
        title: {
          text: 'Exchange rate'
        }
      },
      tooltip: {
        shared: true,
        crosshairs: true
      },
      series: data
    });
  });

  $.getJSON('http://139.196.37.92:8000/futures-api/web/hedging/daily/1', function(data) {
    $('#container3').highcharts({
      chart: {
        zoomType: 'x'
      },
      title: {
        text: 'USD to EUR exchange rate over time'
      },
      subtitle: {
        text: document.ontouchstart === undefined ?
          'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
      },
      xAxis: {
        type: "datetime"
      },
      yAxis: {
        title: {
          text: 'Exchange rate'
        }
      },
      tooltip: {
        shared: true,
        crosshairs: true
      },
      series: data
    });
  });

  $.getJSON('http://139.196.37.92:8000/futures-api/web/hedging/daily/2', function(data) {
    $('#container4').highcharts({
      chart: {
        zoomType: 'x'
      },
      title: {
        text: 'USD to EUR exchange rate over time'
      },
      subtitle: {
        text: document.ontouchstart === undefined ?
          'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
      },
      xAxis: {
        type: "datetime"
      },
      yAxis: {
        title: {
          text: 'Exchange rate'
        }
      },
      tooltip: {
        shared: true,
        crosshairs: true
      },
      series: data
    });
  });

}]);

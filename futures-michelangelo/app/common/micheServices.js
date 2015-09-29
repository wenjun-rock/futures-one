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
]);

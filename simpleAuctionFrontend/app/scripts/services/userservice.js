'use strict';

/**
 * @ngdoc service
 * @name simpleAuctionFrontendApp.userService
 * @description
 * # userService
 * Service in the simpleAuctionFrontendApp.
 */
angular.module('simpleAuctionFrontendApp')
  .service('userService', ['$http', function ($http) {
  
	var baseUrl = 'http://localhost:8080';
	  
    return {
    	userLogin: function(user) {
			return $http.post(baseUrl + '/user/login', user);
		},
		addUser: function(user) {
			return $http.post(baseUrl + '/user', user);
		},
    	getUsers: function(authKey) {
    		var headers = {"Authorization": "Basic " + 'user:'+authKey};
			return $http.get(baseUrl + '/users', {headers: headers});
		}
    }
    
  }]);

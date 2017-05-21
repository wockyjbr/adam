'use strict';

/**
 * @ngdoc service
 * @name simpleAuctionFrontendApp.auctionService
 * @description
 * # auctionService
 * Service in the simpleAuctionFrontendApp.
 */
angular.module('simpleAuctionFrontendApp')
  .service('auctionService', ['$http', function ($http) {
    
    var baseUrl = 'http://localhost:8080';
	  
    return {
    	getAuctions: function(authKey, page) {
    		var headers = {"Authorization": "Basic " + 'user:'+authKey};
			return $http.get(baseUrl + '/auctions?page='+page, {headers: headers});
		},
		getOpenedAuctions: function(authKey, page) {
			var headers = {"Authorization": "Basic " + 'user:'+authKey};
			return $http.get(baseUrl + '/auctions/opened?page='+page, {headers: headers});
		},
		getClosedAuctions: function(authKey, page) {
			var headers = {"Authorization": "Basic " + 'user:'+authKey};
			return $http.get(baseUrl + '/auctions/closed?page='+page, {headers: headers});
		},
		getBidders: function(authKey, auctionId) {
			var headers = {"Authorization": "Basic " + 'user:'+authKey};
			return $http.get(baseUrl + '/auction/'+auctionId+'/bidders', {headers: headers});
		},
		bidAuction: function(authKey, auctionId, user) {
			var headers = {"Authorization": "Basic " + 'user:'+authKey};
			return $http.put(baseUrl + '/auction/'+auctionId+'/bid', user, {headers: headers});
		},
		closeAuction: function(authKey, auctionId, user) {
			var headers = {"Authorization": "Basic " + 'user:'+authKey};
			return $http.put(baseUrl + '/auction/'+auctionId+'/close', user, {headers: headers});
		},
		addAuction: function(authKey, auction) {
			var headers = {"Authorization": "Basic " + 'user:'+authKey};
			return $http.post(baseUrl + '/auction', auction, {headers: headers});
		}
    }
  }]);

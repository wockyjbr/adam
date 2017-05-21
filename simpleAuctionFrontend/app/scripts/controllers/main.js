'use strict';

/**
 * @ngdoc function
 * @name simpleAuctionFrontendApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the simpleAuctionFrontendApp
 */


 
angular.module('simpleAuctionFrontendApp')
  .controller('MainCtrl', ['$scope', '$uibModal', 'userService', 'auctionService', function($scope, $uibModal, userService, auctionService) {
  	
  	$scope.pagination = {
		currentPage: 1,
		totalItems: 0,
		totalPages: 0
	};
  	$scope.currentState = 'all';
  	$scope.currentUser = { id: '', login: '' };
  	$scope.connected = false;
  	$scope.loginMessage = '';
  	var authKey = '';

	$scope.getAuctions = function() {
		$scope.currentState = 'all';
		return auctionService.getAuctions(authKey, $scope.pagination.currentPage-1).then(function(response) {
			$scope.auctions = response.data.content;
			$scope.pagination.totalPages = response.data.totalPages;
			$scope.pagination.totalItems = response.data.totalElements;
			$scope.pagination.currentPage = response.data.number+1;
			$scope.connected = true;
		});
    };
    
    $scope.getOpenedAuctions = function() {
    	$scope.currentState = 'opened';
		return auctionService.getOpenedAuctions(authKey, $scope.pagination.currentPage-1).then(function(response) {
			$scope.auctions = response.data.content;
			$scope.pagination.totalPages = response.data.totalPages;
			$scope.pagination.totalItems = response.data.totalElements;
			$scope.pagination.currentPage = response.data.number+1;
		});
    }
    
    $scope.getClosedAuctions = function() {
    	$scope.currentState = 'closed';
		return auctionService.getClosedAuctions(authKey, $scope.pagination.currentPage-1).then(function(response) {
			$scope.auctions = response.data.content;
			$scope.pagination.totalPages = response.data.totalPages;
			$scope.pagination.totalItems = response.data.totalElements;
			$scope.pagination.currentPage = response.data.number+1;
		});
    }
  
	$scope.addAuction = function () {
		var mainCtrl = this;
		var modalInstance = $uibModal.open({
		  ariaLabelledBy: 'modal-title',
		  ariaDescribedBy: 'modal-body',
		  templateUrl: 'views/auction.html',
		  controller: ['$scope', function($scope) { 
		  	var auctionCtrl = this;
		  	
		  	auctionCtrl.cancel = function () {
    			$scope.$dismiss('cancel');
			};
			
			auctionCtrl.addAuction = function(){
				var auction = {title: auctionCtrl.auctionTitle, price: auctionCtrl.auctionPrice, description: auctionCtrl.auctionDescription, ownerId: mainCtrl.currentUser.id, validity: auctionCtrl.auctionValidity }
				return auctionService.addAuction(authKey, auction).then(function(response) {
					auctionCtrl.cancel();
					mainCtrl.refreshAuctions();
				}, function(data){
					var errMsg = '';
					data.data.errors.forEach(function(err){
						errMsg += err.field.charAt(0).toUpperCase() + err.field.slice(1) + ': ' + err.defaultMessage + '. '; 
					});
					auctionCtrl.validationError = 'Some of the fields are invalid: ' + errMsg;
				});
			}
			
		  }],
		  controllerAs: '$ctrl'
		});
	}
	
	$scope.login = function(){
		var user = {login: $scope.userLogin, password: $scope.userPassword};
		return userService.userLogin(user).then(function(response) {
			if(response.data != null && response.data.Type == 'Success') {
				$scope.connected = true;
				$scope.currentUser.login = response.data.UserLogin;
				$scope.currentUser.id = response.data.UserID;
				$scope.loginMessage = 'Logged as: ' + response.data.UserLogin;
				authKey = response.data.AuthKey;
				$scope.getAuctions();
			} else {
				$scope.connected = false;
				authKey = '';
				$scope.loginMessage = 'Wrong login and/or password.';
			}
		});
	}
	
	$scope.logout = function(){
		$scope.loginMessage = '';
		$scope.connected = false;
	}
 	
 	$scope.register = function(){
		var mainCtrl = this;
		var modalInstance = $uibModal.open({
		  ariaLabelledBy: 'modal-title',
		  ariaDescribedBy: 'modal-body',
		  templateUrl: 'views/register.html',
		  controller: ['$scope', function($scope) {
		  	var registerCtrl = this;
		  	
		  	registerCtrl.cancel = function () {
				$scope.$dismiss('cancel');
			};
			
			function validatePasswordRepeat(pass1, pass2){
				return registerCtrl.userPassword && registerCtrl.userPassword == registerCtrl.userPasswordRepeat;
			}
			
			registerCtrl.addUser = function(){
				if(validatePasswordRepeat(registerCtrl.userPassword, registerCtrl.userPasswordRepeat)) {
					var user = { firstName: registerCtrl.userFirstName, lastName: registerCtrl.userLastName, login: registerCtrl.userLogin, password: registerCtrl.userPassword }
					return userService.addUser(user).then(function(response) {
						registerCtrl.cancel();
						if(response.data.firstName)
							displayModalInfo("New user successfully registered.");
						else
							displayModalInfo("Failed to register new user.");
					}, function(data){
						var errMsg = '';
						data.data.errors.forEach(function(err){
							errMsg += err.field.charAt(0).toUpperCase() + err.field.slice(1) + ': ' + err.defaultMessage + '. '; 
						});
						registerCtrl.validationError = 'Some of the fields are invalid: ' + errMsg;
					});
				} else {
					registerCtrl.validationError = 'Some of the fields are invalid: Password and Password Repeat';
				}
			}
		
		  }],
		  controllerAs: '$ctrl'
		});
	}
	
	$scope.bid = function(auctionId){
		return auctionService.bidAuction(authKey, auctionId, $scope.currentUser).then(function(response) {
			if (response.data.price && response.data.price != null)
				$scope.refreshAuctions();
			else
				displayModalInfo("You cannot bid this item. You are already the top bidder.");
		});
	}
	
	$scope.close = function(auctionId){
		return auctionService.closeAuction(authKey, auctionId, $scope.currentUser).then(function(response) {
			if (response.data.price && response.data.price != null)
				$scope.refreshAuctions();
			else
				displayModalInfo("You cannot close this auction.");
		});
	}
	
	$scope.pageChanged = function() {
	  $scope.refreshAuctions();
	};
	
	$scope.refreshAuctions = function(){
		console.log('Refreshed');
		if($scope.currentState == 'opened')
			$scope.getOpenedAuctions();
		else if ($scope.currentState == 'closed')
			$scope.getClosedAuctions();
		else
			$scope.getAuctions();
	}
	
	$scope.showBidders = function(auctionId) {
		return auctionService.getBidders(authKey, auctionId).then(function(response) {
			var biddersString = '';
			response.data.forEach(function(bidder){
				biddersString += bidder.login + ', ';
			});
			biddersString = biddersString.slice(0,-2);
			displayModalInfo('Bidding history: ' + biddersString + '.');
		});
	};
	
	$scope.calculateRemainingDays = function(closeDate) {
		var today = new Date();
		var diffDays = Math.round(Math.abs((today.getTime() - closeDate)/(24*60*60*1000)));
		return diffDays + ' days';
	}
	
	function displayModalInfo(msg){
		var infoMessage = msg;
		var modalInstance = $uibModal.open({
		  ariaLabelledBy: 'modal-title',
		  ariaDescribedBy: 'modal-body',
		  templateUrl: 'views/info.html',
		  controller: ['$scope', function($scope) {
		  	this.cancel = function () {
    			$scope.$dismiss('cancel');
			};			
			this.infoMessage = infoMessage;
		  }],
		  controllerAs: '$ctrl'
		});
	}
 	
  }]);

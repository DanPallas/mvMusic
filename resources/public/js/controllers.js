'use strict';

var mvControllers = angular.module('mvControllers',[]);

mvControllers.controller('mvCtrl',['$scope',
		function($scope){
			$scope.order = '+';
			$scope.setOrder = function(ord){
				$scope.order = ord;
			};
			$scope.toggleOrder = function(){
				if($scope.order === '+')
					$scope.order = '-';
				else
					$scope.order = '+';
			};
			$scope.getOrderLabel = function(){
				if($scope.order === '+')
					return 'A-Z';
				else
					return 'Z-A';
			};
			$scope.context = 'artists';
			$scope.setContext = function(context){
				$scope.context = context;
			};
			$scope.isActive = function(context){
				if(context === $scope.context)
					return 'active';
				else
					return '';
			};
		}]);

mvControllers.controller('ArtistsCtrl',['$scope','ArtistSvc',
		function($scope, ArtistSvc){
			$scope.artists = ArtistSvc.query();
			$scope.order = function(){
				return $scope.$parent.order + 'sort';
			};
			$scope.utils = mvUtilities.utils;
		}]);

mvControllers.controller('ArtistCtrl',['$scope','ArtistSvc',
		function($scope, ArtistSvc, artist){
			$scope.albums = null;
			$scope.showAlbums = false;
			$scope.toggleSymbol = function(){
				if($scope.showAlbums)
					return '-';
				else
					return '+';
			};
			$scope.toggleAlbums = function(){ 
				if($scope.albums === null)
					$scope.albums = ArtistSvc.query({artist:$scope.artist.artist});
				if($scope.showAlbums === false)
					$scope.showAlbums = true;
				else
					$scope.showAlbums = false;
			};
			$scope.utils = mvUtilities.utils;
		}]);

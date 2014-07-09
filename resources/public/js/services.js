'user strict';

var mvServices = angular.module('mvServices',['ngResource']);

mvServices.factory('ArtistSvc',['$resource',
		function($resource){
			return $resource('artists/:artist',{},{
				query: {method: 'GET', params: {artist: 'all'}, isArray:true}
			});
		}]);

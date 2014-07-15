'use strict';

var mvMusicMod = angular.module('mvMusic', [
		'mvControllers',
		'mvServices',
		'ngAnimate'
		]);

var mvUtilities = mvUtilities || {};

mvUtilities.utils = {
	formatTime: function(seconds){
		var hours = parseInt(seconds/3600,10);
		seconds = seconds - hours * 216000;
		var minutes = parseInt(seconds/60,10);
		seconds = seconds - minutes*60;
		var time = '';
		if(seconds < 10)
			time = '0' + seconds;
		else
			time = '' + seconds;
		if(minutes < 10) 
			time = '0' + minutes + ':' + time;
		else
			time = minutes + ':' + time;
		if(hours > 0)
			time = hours + time;
		return time;
	}
};

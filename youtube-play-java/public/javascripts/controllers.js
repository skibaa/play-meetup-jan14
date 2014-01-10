var youtubeSearch = angular.module('youtubeSearch', []);

youtubeSearch.controller('YouTubeSearchCtrl', function ($scope) {

    $scope.searchQuery = ""
    $scope.searchError = null

    $scope.onSearch = function () {
        var searchRoute = jsRoutes.controllers.Application.search($scope.searchQuery)

        searchRoute.ajax( ).done(function(res) {
            $scope.searchError = null
            $scope.searchResults = res
            $scope.$apply()
        } ).fail(function(res) {
            $scope.searchError = res.responseText
            $scope.$apply()
        });
    }

    $scope.playError = false
    $scope.playStatus = null

    $scope.onPlay = function (playerId, videoId, thumbnailUrl) {
        var playRoute = jsRoutes.controllers.Application.playVideo(playerId, videoId, thumbnailUrl)

        playRoute.ajax().done (function(res) {
            $scope.playError = res.error
            $scope.playStatus = res.status
            $scope.$apply()
        }).fail(function(res) {
            $scope.playError = res.responseText
            $scope.playStatus = null
            $scope.$apply()
        })
    }
});
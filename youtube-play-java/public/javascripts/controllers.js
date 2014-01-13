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
});
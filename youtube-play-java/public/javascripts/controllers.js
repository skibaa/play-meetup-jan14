var youtubeSearch = angular.module('youtubeSearch', []);

youtubeSearch.controller('YouTubeSearchCtrl', function ($scope) {

    $scope.searchResults = [
        {'title': 'Title 2', 'videoId':'bbb', 'description':'description 2',
            'thumbnail':'http://stereo.gsfc.nasa.gov/beacon/euvi_195_rotated.gif'}
    ]

    $scope.searchQuery = ""
    $scope.searchError = null

    $scope.onSearch = function () {
        var searchRoute = jsRoutes.controllers.Application.search($scope.searchQuery)

        searchRoute.ajax( ).done(function(res) {
            $scope.searchError = null
            $scope.searchResults.push({'title': 'Title 1', 'videoId':'ooo', 'description':res,
                'thumbnail':'http://digital-photography-school.com/wp-content/uploads/2013/03/Acorn256.png'})
            $scope.$apply()
        } ).fail(function(res) {
            $scope.searchError = res.responseText
            $scope.$apply()
        });
    }
});
var youtubeSearch = angular.module('youtubeSearch', []);

youtubeSearch.controller('YouTubeSearchCtrl', function ($scope) {

    $scope.searchResults = [
        {'title': 'Title 1', 'videoId':'ooo', 'description':'description 1',
            'thumbnail':'http://digital-photography-school.com/wp-content/uploads/2013/03/Acorn256.png'},
        {'title': 'Title 2', 'videoId':'bbb', 'description':'description 2',
            'thumbnail':'http://stereo.gsfc.nasa.gov/beacon/euvi_195_rotated.gif'}
    ]

});
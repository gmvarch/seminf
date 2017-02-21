(function() {
    'use strict';

    angular
        .module('techhipApp')
        .controller('VoteDetailController', VoteDetailController);

    VoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vote'];

    function VoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Vote) {
        var vm = this;

        vm.vote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('techhipApp:voteUpdate', function(event, result) {
            vm.vote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

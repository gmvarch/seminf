(function() {
    'use strict';

    angular
        .module('techhipApp')
        .controller('OptionDetailController', OptionDetailController);

    OptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Option'];

    function OptionDetailController($scope, $rootScope, $stateParams, previousState, entity, Option) {
        var vm = this;

        vm.option = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('techhipApp:optionUpdate', function(event, result) {
            vm.option = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

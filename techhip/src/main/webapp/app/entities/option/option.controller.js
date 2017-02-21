(function() {
    'use strict';

    angular
        .module('techhipApp')
        .controller('OptionController', OptionController);

    OptionController.$inject = ['$scope', '$state', 'Option'];

    function OptionController ($scope, $state, Option) {
        var vm = this;

        vm.options = [];

        loadAll();

        function loadAll() {
            Option.query(function(result) {
                vm.options = result;
                vm.searchQuery = null;
            });
        }
    }
})();

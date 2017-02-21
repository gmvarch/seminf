(function() {
    'use strict';

    angular
        .module('techhipApp')
        .controller('SuccessController', SuccessController);

    SuccessController.$inject = ['$scope', '$state'];

    function SuccessController ($scope, $state) {
        var vm = this;
        vm.results = results;        
       
        function results () {
            $state.go('results');
        }
        
    }
})();

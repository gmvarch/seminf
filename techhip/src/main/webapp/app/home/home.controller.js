(function() {
    'use strict';

    angular
        .module('techhipApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Option', 'Vote'];

    function HomeController ($scope, Principal, LoginService, $state, Option, Vote) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });
        vm.saveVote = saveVote;	
        
        getAccount();
        getOptions();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        
        function getOptions (){
        	Option.query(function(result) {
                vm.options = result;
                vm.searchQuery = null;
            });
        }
        
        function saveVote (valor) {
            vm.isSaving = true;
            vm.vote = new Vote;
            vm.vote.source = 'no importa';
            vm.vote.value = valor;
            Vote.save(vm.vote, onSaveSuccess, onSaveError);            
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $state.go("success",null,{reload: true});
        }

        function onSaveError () {
            vm.isSaving = false;
            $state.go("fallo",null,{reload: true});
        }

    }
})();

(function() {
    'use strict';

    angular
        .module('techhipApp')
        .controller('OptionDialogController', OptionDialogController);

    OptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Option'];

    function OptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Option) {
        var vm = this;

        vm.option = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.option.id !== null) {
                Option.update(vm.option, onSaveSuccess, onSaveError);
            } else {
                Option.save(vm.option, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('techhipApp:optionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

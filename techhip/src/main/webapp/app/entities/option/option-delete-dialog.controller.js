(function() {
    'use strict';

    angular
        .module('techhipApp')
        .controller('OptionDeleteController',OptionDeleteController);

    OptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Option'];

    function OptionDeleteController($uibModalInstance, entity, Option) {
        var vm = this;

        vm.option = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Option.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('OperationAppDeleteController',OperationAppDeleteController);

    OperationAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'Operation'];

    function OperationAppDeleteController($uibModalInstance, entity, Operation) {
        var vm = this;

        vm.operation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Operation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('EmployeeAppDeleteController',EmployeeAppDeleteController);

    EmployeeAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'Employee'];

    function EmployeeAppDeleteController($uibModalInstance, entity, Employee) {
        var vm = this;

        vm.employee = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Employee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

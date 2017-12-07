(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintenanceAppDeleteController',MaintenanceAppDeleteController);

    MaintenanceAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'Maintenance'];

    function MaintenanceAppDeleteController($uibModalInstance, entity, Maintenance) {
        var vm = this;

        vm.maintenance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Maintenance.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

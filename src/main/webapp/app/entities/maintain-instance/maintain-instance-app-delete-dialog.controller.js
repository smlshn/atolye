(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintainInstanceAppDeleteController',MaintainInstanceAppDeleteController);

    MaintainInstanceAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'MaintainInstance'];

    function MaintainInstanceAppDeleteController($uibModalInstance, entity, MaintainInstance) {
        var vm = this;

        vm.maintainInstance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MaintainInstance.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

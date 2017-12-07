(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintainTypeAppDeleteController',MaintainTypeAppDeleteController);

    MaintainTypeAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'MaintainType'];

    function MaintainTypeAppDeleteController($uibModalInstance, entity, MaintainType) {
        var vm = this;

        vm.maintainType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MaintainType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

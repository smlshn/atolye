(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('SystemUserAppDeleteController',SystemUserAppDeleteController);

    SystemUserAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'SystemUser'];

    function SystemUserAppDeleteController($uibModalInstance, entity, SystemUser) {
        var vm = this;

        vm.systemUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SystemUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

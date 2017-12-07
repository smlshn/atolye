(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CompanyUserAppDeleteController',CompanyUserAppDeleteController);

    CompanyUserAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'CompanyUser'];

    function CompanyUserAppDeleteController($uibModalInstance, entity, CompanyUser) {
        var vm = this;

        vm.companyUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CompanyUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

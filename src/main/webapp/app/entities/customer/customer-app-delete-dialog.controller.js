(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CustomerAppDeleteController',CustomerAppDeleteController);

    CustomerAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'Customer'];

    function CustomerAppDeleteController($uibModalInstance, entity, Customer) {
        var vm = this;

        vm.customer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Customer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

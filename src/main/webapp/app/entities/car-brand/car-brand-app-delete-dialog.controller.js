(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CarBrandAppDeleteController',CarBrandAppDeleteController);

    CarBrandAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'CarBrand'];

    function CarBrandAppDeleteController($uibModalInstance, entity, CarBrand) {
        var vm = this;

        vm.carBrand = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CarBrand.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

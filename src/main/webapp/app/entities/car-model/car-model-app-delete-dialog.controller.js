(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CarModelAppDeleteController',CarModelAppDeleteController);

    CarModelAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'CarModel'];

    function CarModelAppDeleteController($uibModalInstance, entity, CarModel) {
        var vm = this;

        vm.carModel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CarModel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

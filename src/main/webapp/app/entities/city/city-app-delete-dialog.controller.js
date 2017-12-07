(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CityAppDeleteController',CityAppDeleteController);

    CityAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'City'];

    function CityAppDeleteController($uibModalInstance, entity, City) {
        var vm = this;

        vm.city = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            City.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

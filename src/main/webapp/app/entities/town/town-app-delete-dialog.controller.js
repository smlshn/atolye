(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('TownAppDeleteController',TownAppDeleteController);

    TownAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'Town'];

    function TownAppDeleteController($uibModalInstance, entity, Town) {
        var vm = this;

        vm.town = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Town.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

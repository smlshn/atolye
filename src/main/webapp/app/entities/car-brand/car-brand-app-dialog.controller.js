(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CarBrandAppDialogController', CarBrandAppDialogController);

    CarBrandAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CarBrand'];

    function CarBrandAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CarBrand) {
        var vm = this;

        vm.carBrand = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.carBrand.id !== null) {
                CarBrand.update(vm.carBrand, onSaveSuccess, onSaveError);
            } else {
                CarBrand.save(vm.carBrand, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('atolyeApp:carBrandUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

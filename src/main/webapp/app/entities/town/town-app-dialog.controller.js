(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('TownAppDialogController', TownAppDialogController);

    TownAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Town', 'City'];

    function TownAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Town, City) {
        var vm = this;

        vm.town = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cities = City.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.town.id !== null) {
                Town.update(vm.town, onSaveSuccess, onSaveError);
            } else {
                Town.save(vm.town, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('atolyeApp:townUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

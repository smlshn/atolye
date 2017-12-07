(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintainTypeAppDialogController', MaintainTypeAppDialogController);

    MaintainTypeAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MaintainType'];

    function MaintainTypeAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MaintainType) {
        var vm = this;

        vm.maintainType = entity;
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
            if (vm.maintainType.id !== null) {
                MaintainType.update(vm.maintainType, onSaveSuccess, onSaveError);
            } else {
                MaintainType.save(vm.maintainType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('atolyeApp:maintainTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

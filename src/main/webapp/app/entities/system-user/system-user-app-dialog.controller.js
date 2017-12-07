(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('SystemUserAppDialogController', SystemUserAppDialogController);

    SystemUserAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SystemUser'];

    function SystemUserAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SystemUser) {
        var vm = this;

        vm.systemUser = entity;
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
            if (vm.systemUser.id !== null) {
                SystemUser.update(vm.systemUser, onSaveSuccess, onSaveError);
            } else {
                SystemUser.save(vm.systemUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('atolyeApp:systemUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

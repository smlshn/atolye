(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CompanyUserAppDialogController', CompanyUserAppDialogController);

    CompanyUserAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyUser', 'Company'];

    function CompanyUserAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyUser, Company) {
        var vm = this;

        vm.companyUser = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyUser.id !== null) {
                CompanyUser.update(vm.companyUser, onSaveSuccess, onSaveError);
            } else {
                CompanyUser.save(vm.companyUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('atolyeApp:companyUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

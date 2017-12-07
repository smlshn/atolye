(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintenanceAppDialogController', MaintenanceAppDialogController);

    MaintenanceAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Maintenance', 'Company', 'MaintainType'];

    function MaintenanceAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Maintenance, Company, MaintainType) {
        var vm = this;

        vm.maintenance = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.maintaintypes = MaintainType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.maintenance.id !== null) {
                Maintenance.update(vm.maintenance, onSaveSuccess, onSaveError);
            } else {
                Maintenance.save(vm.maintenance, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('atolyeApp:maintenanceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

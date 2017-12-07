(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintainInstanceAppDialogController', MaintainInstanceAppDialogController);

    MaintainInstanceAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MaintainInstance', 'Employee', 'Operation', 'Maintenance'];

    function MaintainInstanceAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MaintainInstance, Employee, Operation, Maintenance) {
        var vm = this;

        vm.maintainInstance = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query();
        vm.operations = Operation.query();
        vm.maintenances = Maintenance.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.maintainInstance.id !== null) {
                MaintainInstance.update(vm.maintainInstance, onSaveSuccess, onSaveError);
            } else {
                MaintainInstance.save(vm.maintainInstance, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('atolyeApp:maintainInstanceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

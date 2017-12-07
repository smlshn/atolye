(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('OperationAppDialogController', OperationAppDialogController);

    OperationAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Operation', 'Company', 'Car'];

    function OperationAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Operation, Company, Car) {
        var vm = this;

        vm.operation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.cars = Car.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.operation.id !== null) {
                Operation.update(vm.operation, onSaveSuccess, onSaveError);
            } else {
                Operation.save(vm.operation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('atolyeApp:operationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

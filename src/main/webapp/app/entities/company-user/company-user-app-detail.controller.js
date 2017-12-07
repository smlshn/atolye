(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CompanyUserAppDetailController', CompanyUserAppDetailController);

    CompanyUserAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CompanyUser', 'Company'];

    function CompanyUserAppDetailController($scope, $rootScope, $stateParams, previousState, entity, CompanyUser, Company) {
        var vm = this;

        vm.companyUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:companyUserUpdate', function(event, result) {
            vm.companyUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

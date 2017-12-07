(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('SystemUserAppController', SystemUserAppController);

    SystemUserAppController.$inject = ['SystemUser', 'SystemUserSearch'];

    function SystemUserAppController(SystemUser, SystemUserSearch) {

        var vm = this;

        vm.systemUsers = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            SystemUser.query(function(result) {
                vm.systemUsers = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SystemUserSearch.query({query: vm.searchQuery}, function(result) {
                vm.systemUsers = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();

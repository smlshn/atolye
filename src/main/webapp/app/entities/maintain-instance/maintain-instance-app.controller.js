(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintainInstanceAppController', MaintainInstanceAppController);

    MaintainInstanceAppController.$inject = ['MaintainInstance', 'MaintainInstanceSearch'];

    function MaintainInstanceAppController(MaintainInstance, MaintainInstanceSearch) {

        var vm = this;

        vm.maintainInstances = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MaintainInstance.query(function(result) {
                vm.maintainInstances = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MaintainInstanceSearch.query({query: vm.searchQuery}, function(result) {
                vm.maintainInstances = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();

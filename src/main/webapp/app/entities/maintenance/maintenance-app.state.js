(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('maintenance-app', {
            parent: 'entity',
            url: '/maintenance-app?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.maintenance.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/maintenance/maintenancesapp.html',
                    controller: 'MaintenanceAppController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('maintenance');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('maintenance-app-detail', {
            parent: 'maintenance-app',
            url: '/maintenance-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.maintenance.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/maintenance/maintenance-app-detail.html',
                    controller: 'MaintenanceAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('maintenance');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Maintenance', function($stateParams, Maintenance) {
                    return Maintenance.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'maintenance-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('maintenance-app-detail.edit', {
            parent: 'maintenance-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintenance/maintenance-app-dialog.html',
                    controller: 'MaintenanceAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Maintenance', function(Maintenance) {
                            return Maintenance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('maintenance-app.new', {
            parent: 'maintenance-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintenance/maintenance-app-dialog.html',
                    controller: 'MaintenanceAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                price: null,
                                priceSecond: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('maintenance-app', null, { reload: 'maintenance-app' });
                }, function() {
                    $state.go('maintenance-app');
                });
            }]
        })
        .state('maintenance-app.edit', {
            parent: 'maintenance-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintenance/maintenance-app-dialog.html',
                    controller: 'MaintenanceAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Maintenance', function(Maintenance) {
                            return Maintenance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('maintenance-app', null, { reload: 'maintenance-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('maintenance-app.delete', {
            parent: 'maintenance-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintenance/maintenance-app-delete-dialog.html',
                    controller: 'MaintenanceAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Maintenance', function(Maintenance) {
                            return Maintenance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('maintenance-app', null, { reload: 'maintenance-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

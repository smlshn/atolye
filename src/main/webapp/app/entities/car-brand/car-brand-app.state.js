(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('car-brand-app', {
            parent: 'entity',
            url: '/car-brand-app?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.carBrand.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-brand/car-brandsapp.html',
                    controller: 'CarBrandAppController',
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
                    $translatePartialLoader.addPart('carBrand');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('car-brand-app-detail', {
            parent: 'car-brand-app',
            url: '/car-brand-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.carBrand.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-brand/car-brand-app-detail.html',
                    controller: 'CarBrandAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carBrand');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CarBrand', function($stateParams, CarBrand) {
                    return CarBrand.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'car-brand-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('car-brand-app-detail.edit', {
            parent: 'car-brand-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-brand/car-brand-app-dialog.html',
                    controller: 'CarBrandAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarBrand', function(CarBrand) {
                            return CarBrand.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-brand-app.new', {
            parent: 'car-brand-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-brand/car-brand-app-dialog.html',
                    controller: 'CarBrandAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('car-brand-app', null, { reload: 'car-brand-app' });
                }, function() {
                    $state.go('car-brand-app');
                });
            }]
        })
        .state('car-brand-app.edit', {
            parent: 'car-brand-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-brand/car-brand-app-dialog.html',
                    controller: 'CarBrandAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarBrand', function(CarBrand) {
                            return CarBrand.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-brand-app', null, { reload: 'car-brand-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-brand-app.delete', {
            parent: 'car-brand-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-brand/car-brand-app-delete-dialog.html',
                    controller: 'CarBrandAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CarBrand', function(CarBrand) {
                            return CarBrand.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-brand-app', null, { reload: 'car-brand-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

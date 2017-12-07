(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('car-app', {
            parent: 'entity',
            url: '/car-app?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.car.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car/carsapp.html',
                    controller: 'CarAppController',
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
                    $translatePartialLoader.addPart('car');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('car-app-detail', {
            parent: 'car-app',
            url: '/car-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.car.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car/car-app-detail.html',
                    controller: 'CarAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('car');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Car', function($stateParams, Car) {
                    return Car.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'car-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('car-app-detail.edit', {
            parent: 'car-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car/car-app-dialog.html',
                    controller: 'CarAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Car', function(Car) {
                            return Car.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-app.new', {
            parent: 'car-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car/car-app-dialog.html',
                    controller: 'CarAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                plate: null,
                                chaseNo: null,
                                km: null,
                                latestOilChangeKm: null,
                                lastVisit: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('car-app', null, { reload: 'car-app' });
                }, function() {
                    $state.go('car-app');
                });
            }]
        })
        .state('car-app.edit', {
            parent: 'car-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car/car-app-dialog.html',
                    controller: 'CarAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Car', function(Car) {
                            return Car.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-app', null, { reload: 'car-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-app.delete', {
            parent: 'car-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car/car-app-delete-dialog.html',
                    controller: 'CarAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Car', function(Car) {
                            return Car.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-app', null, { reload: 'car-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

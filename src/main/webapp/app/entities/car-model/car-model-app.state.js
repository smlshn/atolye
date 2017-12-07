(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('car-model-app', {
            parent: 'entity',
            url: '/car-model-app',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.carModel.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-model/car-modelsapp.html',
                    controller: 'CarModelAppController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carModel');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('car-model-app-detail', {
            parent: 'car-model-app',
            url: '/car-model-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.carModel.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-model/car-model-app-detail.html',
                    controller: 'CarModelAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carModel');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CarModel', function($stateParams, CarModel) {
                    return CarModel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'car-model-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('car-model-app-detail.edit', {
            parent: 'car-model-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model/car-model-app-dialog.html',
                    controller: 'CarModelAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarModel', function(CarModel) {
                            return CarModel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-model-app.new', {
            parent: 'car-model-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model/car-model-app-dialog.html',
                    controller: 'CarModelAppDialogController',
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
                    $state.go('car-model-app', null, { reload: 'car-model-app' });
                }, function() {
                    $state.go('car-model-app');
                });
            }]
        })
        .state('car-model-app.edit', {
            parent: 'car-model-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model/car-model-app-dialog.html',
                    controller: 'CarModelAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarModel', function(CarModel) {
                            return CarModel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-model-app', null, { reload: 'car-model-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-model-app.delete', {
            parent: 'car-model-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model/car-model-app-delete-dialog.html',
                    controller: 'CarModelAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CarModel', function(CarModel) {
                            return CarModel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-model-app', null, { reload: 'car-model-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

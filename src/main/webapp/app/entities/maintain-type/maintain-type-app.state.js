(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('maintain-type-app', {
            parent: 'entity',
            url: '/maintain-type-app',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.maintainType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/maintain-type/maintain-typesapp.html',
                    controller: 'MaintainTypeAppController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('maintainType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('maintain-type-app-detail', {
            parent: 'maintain-type-app',
            url: '/maintain-type-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.maintainType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/maintain-type/maintain-type-app-detail.html',
                    controller: 'MaintainTypeAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('maintainType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MaintainType', function($stateParams, MaintainType) {
                    return MaintainType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'maintain-type-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('maintain-type-app-detail.edit', {
            parent: 'maintain-type-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintain-type/maintain-type-app-dialog.html',
                    controller: 'MaintainTypeAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MaintainType', function(MaintainType) {
                            return MaintainType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('maintain-type-app.new', {
            parent: 'maintain-type-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintain-type/maintain-type-app-dialog.html',
                    controller: 'MaintainTypeAppDialogController',
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
                    $state.go('maintain-type-app', null, { reload: 'maintain-type-app' });
                }, function() {
                    $state.go('maintain-type-app');
                });
            }]
        })
        .state('maintain-type-app.edit', {
            parent: 'maintain-type-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintain-type/maintain-type-app-dialog.html',
                    controller: 'MaintainTypeAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MaintainType', function(MaintainType) {
                            return MaintainType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('maintain-type-app', null, { reload: 'maintain-type-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('maintain-type-app.delete', {
            parent: 'maintain-type-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintain-type/maintain-type-app-delete-dialog.html',
                    controller: 'MaintainTypeAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MaintainType', function(MaintainType) {
                            return MaintainType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('maintain-type-app', null, { reload: 'maintain-type-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('maintain-instance-app', {
            parent: 'entity',
            url: '/maintain-instance-app',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.maintainInstance.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/maintain-instance/maintain-instancesapp.html',
                    controller: 'MaintainInstanceAppController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('maintainInstance');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('maintain-instance-app-detail', {
            parent: 'maintain-instance-app',
            url: '/maintain-instance-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.maintainInstance.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/maintain-instance/maintain-instance-app-detail.html',
                    controller: 'MaintainInstanceAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('maintainInstance');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MaintainInstance', function($stateParams, MaintainInstance) {
                    return MaintainInstance.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'maintain-instance-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('maintain-instance-app-detail.edit', {
            parent: 'maintain-instance-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintain-instance/maintain-instance-app-dialog.html',
                    controller: 'MaintainInstanceAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MaintainInstance', function(MaintainInstance) {
                            return MaintainInstance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('maintain-instance-app.new', {
            parent: 'maintain-instance-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintain-instance/maintain-instance-app-dialog.html',
                    controller: 'MaintainInstanceAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                comment: null,
                                price: null,
                                priceSecond: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('maintain-instance-app', null, { reload: 'maintain-instance-app' });
                }, function() {
                    $state.go('maintain-instance-app');
                });
            }]
        })
        .state('maintain-instance-app.edit', {
            parent: 'maintain-instance-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintain-instance/maintain-instance-app-dialog.html',
                    controller: 'MaintainInstanceAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MaintainInstance', function(MaintainInstance) {
                            return MaintainInstance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('maintain-instance-app', null, { reload: 'maintain-instance-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('maintain-instance-app.delete', {
            parent: 'maintain-instance-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/maintain-instance/maintain-instance-app-delete-dialog.html',
                    controller: 'MaintainInstanceAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MaintainInstance', function(MaintainInstance) {
                            return MaintainInstance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('maintain-instance-app', null, { reload: 'maintain-instance-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

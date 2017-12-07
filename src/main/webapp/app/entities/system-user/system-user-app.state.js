(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('system-user-app', {
            parent: 'entity',
            url: '/system-user-app',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.systemUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/system-user/system-usersapp.html',
                    controller: 'SystemUserAppController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('systemUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('system-user-app-detail', {
            parent: 'system-user-app',
            url: '/system-user-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.systemUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/system-user/system-user-app-detail.html',
                    controller: 'SystemUserAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('systemUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SystemUser', function($stateParams, SystemUser) {
                    return SystemUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'system-user-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('system-user-app-detail.edit', {
            parent: 'system-user-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-user/system-user-app-dialog.html',
                    controller: 'SystemUserAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SystemUser', function(SystemUser) {
                            return SystemUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('system-user-app.new', {
            parent: 'system-user-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-user/system-user-app-dialog.html',
                    controller: 'SystemUserAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('system-user-app', null, { reload: 'system-user-app' });
                }, function() {
                    $state.go('system-user-app');
                });
            }]
        })
        .state('system-user-app.edit', {
            parent: 'system-user-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-user/system-user-app-dialog.html',
                    controller: 'SystemUserAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SystemUser', function(SystemUser) {
                            return SystemUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('system-user-app', null, { reload: 'system-user-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('system-user-app.delete', {
            parent: 'system-user-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-user/system-user-app-delete-dialog.html',
                    controller: 'SystemUserAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SystemUser', function(SystemUser) {
                            return SystemUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('system-user-app', null, { reload: 'system-user-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

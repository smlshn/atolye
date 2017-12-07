(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-user-app', {
            parent: 'entity',
            url: '/company-user-app',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.companyUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-user/company-usersapp.html',
                    controller: 'CompanyUserAppController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-user-app-detail', {
            parent: 'company-user-app',
            url: '/company-user-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.companyUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-user/company-user-app-detail.html',
                    controller: 'CompanyUserAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CompanyUser', function($stateParams, CompanyUser) {
                    return CompanyUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company-user-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-user-app-detail.edit', {
            parent: 'company-user-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-user/company-user-app-dialog.html',
                    controller: 'CompanyUserAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyUser', function(CompanyUser) {
                            return CompanyUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-user-app.new', {
            parent: 'company-user-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-user/company-user-app-dialog.html',
                    controller: 'CompanyUserAppDialogController',
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
                    $state.go('company-user-app', null, { reload: 'company-user-app' });
                }, function() {
                    $state.go('company-user-app');
                });
            }]
        })
        .state('company-user-app.edit', {
            parent: 'company-user-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-user/company-user-app-dialog.html',
                    controller: 'CompanyUserAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyUser', function(CompanyUser) {
                            return CompanyUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-user-app', null, { reload: 'company-user-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-user-app.delete', {
            parent: 'company-user-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-user/company-user-app-delete-dialog.html',
                    controller: 'CompanyUserAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanyUser', function(CompanyUser) {
                            return CompanyUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-user-app', null, { reload: 'company-user-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

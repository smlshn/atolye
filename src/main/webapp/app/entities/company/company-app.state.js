(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-app', {
            parent: 'entity',
            url: '/company-app?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.company.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company/companiesapp.html',
                    controller: 'CompanyAppController',
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
                    $translatePartialLoader.addPart('company');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-app-detail', {
            parent: 'company-app',
            url: '/company-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'atolyeApp.company.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company/company-app-detail.html',
                    controller: 'CompanyAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('company');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Company', function($stateParams, Company) {
                    return Company.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-app-detail.edit', {
            parent: 'company-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-app-dialog.html',
                    controller: 'CompanyAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-app.new', {
            parent: 'company-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-app-dialog.html',
                    controller: 'CompanyAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                shortName: null,
                                vkn: null,
                                phone: null,
                                phoneSecond: null,
                                email: null,
                                emailSecond: null,
                                address: null,
                                addressSecond: null,
                                webSite: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company-app', null, { reload: 'company-app' });
                }, function() {
                    $state.go('company-app');
                });
            }]
        })
        .state('company-app.edit', {
            parent: 'company-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-app-dialog.html',
                    controller: 'CompanyAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-app', null, { reload: 'company-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-app.delete', {
            parent: 'company-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-app-delete-dialog.html',
                    controller: 'CompanyAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-app', null, { reload: 'company-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

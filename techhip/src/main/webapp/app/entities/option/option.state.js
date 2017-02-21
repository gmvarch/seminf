(function() {
    'use strict';

    angular
        .module('techhipApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('option', {
            parent: 'entity',
            url: '/option',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Options'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/option/options.html',
                    controller: 'OptionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('option-detail', {
            parent: 'entity',
            url: '/option/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Option'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/option/option-detail.html',
                    controller: 'OptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Option', function($stateParams, Option) {
                    return Option.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'option',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('option-detail.edit', {
            parent: 'option-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/option/option-dialog.html',
                    controller: 'OptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Option', function(Option) {
                            return Option.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('option.new', {
            parent: 'option',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/option/option-dialog.html',
                    controller: 'OptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                option: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('option', null, { reload: 'option' });
                }, function() {
                    $state.go('option');
                });
            }]
        })
        .state('option.edit', {
            parent: 'option',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/option/option-dialog.html',
                    controller: 'OptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Option', function(Option) {
                            return Option.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('option', null, { reload: 'option' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('option.delete', {
            parent: 'option',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/option/option-delete-dialog.html',
                    controller: 'OptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Option', function(Option) {
                            return Option.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('option', null, { reload: 'option' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

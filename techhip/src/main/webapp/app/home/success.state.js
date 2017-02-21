(function() {
    'use strict';

    angular
        .module('techhipApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('results', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/results.html',
                    controller: 'SuccessController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();

(function() {
    'use strict';
    angular
        .module('techhipApp')
        .factory('Option', Option);

    Option.$inject = ['$resource'];

    function Option ($resource) {
        var resourceUrl =  'optionservice/' + 'api/options/:id';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

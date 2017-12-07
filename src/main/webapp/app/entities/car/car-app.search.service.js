(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .factory('CarSearch', CarSearch);

    CarSearch.$inject = ['$resource'];

    function CarSearch($resource) {
        var resourceUrl =  'api/_search/cars/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

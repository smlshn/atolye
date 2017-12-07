(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .factory('MaintenanceSearch', MaintenanceSearch);

    MaintenanceSearch.$inject = ['$resource'];

    function MaintenanceSearch($resource) {
        var resourceUrl =  'api/_search/maintenances/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

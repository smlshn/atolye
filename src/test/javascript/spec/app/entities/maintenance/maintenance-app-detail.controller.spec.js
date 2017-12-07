'use strict';

describe('Controller Tests', function() {

    describe('Maintenance Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMaintenance, MockCompany, MockMaintainType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMaintenance = jasmine.createSpy('MockMaintenance');
            MockCompany = jasmine.createSpy('MockCompany');
            MockMaintainType = jasmine.createSpy('MockMaintainType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Maintenance': MockMaintenance,
                'Company': MockCompany,
                'MaintainType': MockMaintainType
            };
            createController = function() {
                $injector.get('$controller')("MaintenanceAppDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'atolyeApp:maintenanceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

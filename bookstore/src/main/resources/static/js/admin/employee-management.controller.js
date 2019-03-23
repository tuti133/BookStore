(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("EmployeeManagementController", EmployeeManagementController)
    EmployeeManagementController.$inject = ["$scope", "$mdDialog"];

    function EmployeeManagementController($scope, $mdDialog) {
        let vm = this;
        vm.selected = [];

        vm.query = {
            order: 'name',
            limit: 5,
            page: 1
        };

        function success(desserts) {
            vm.desserts = desserts;
        }

        vm.getDesserts = function () {
            vm.promise = $nutrition.desserts.get(vm.query, success).$promise;
        };

        vm.showAdvanced = function(ev) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '/dialog/employee.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:true,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            })
                .then(function(answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function() {
                    $scope.status = 'You cancelled the dialog.';
                });
        };

        function DialogController($scope, $mdDialog) {
            $scope.hide = function() {
                console.log("X1")
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                console.log("X2")
                $mdDialog.cancel();
            };

            $scope.answer = function(answer) {
                console.log("X3")
                $mdDialog.hide(answer);
            };
        }
    }
})();
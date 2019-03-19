(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("EmployeeManagementController", EmployeeManagementController)
    EmployeeManagementController.$inject = ["$scope"];

    function EmployeeManagementController($scope) {
        var vm = $scope;
        vm.abc = "Employee";
    }
})();
(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BillController", BillController)
    BillController.$inject = ["$scope", "$mdDialog", "AccountService"];

    function BillController($scope, $mdDialog, AccountService) {

    }
})();
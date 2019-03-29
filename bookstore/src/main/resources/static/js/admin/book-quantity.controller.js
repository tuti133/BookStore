(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BookQuantityController", BookQuantityController)
    BookQuantityController.$inject = ["$scope", "$mdDialog", "AccountService"];

    function BookQuantityController($scope, $mdDialog, AccountService) {

    }
})();
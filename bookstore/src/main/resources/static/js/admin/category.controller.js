(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("CategoryController", CategoryController)
    CategoryController.$inject = ["$scope", "$mdDialog", "AccountService"];

    function CategoryController($scope, $mdDialog, AccountService) {

    }
})();
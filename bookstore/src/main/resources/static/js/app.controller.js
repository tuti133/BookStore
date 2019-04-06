(function () {
    'use strict'
    angular.module('BookStoreApp').controller("AppController", AppController)

    AppController.$inject = ['$scope', "$rootScope", "BookService"];

    function AppController($scope, $rootScope, BookService) {
        let vm = this;
        vm.cartList = [];

        getCart();

        function getCart() {
            let storage = localStorage.getItem("cartList");
            if (storage != null) {
                vm.cartList = JSON.parse(storage);
            }
        }

        vm.addToCart = function (item) {
            let storage = localStorage.getItem("cartList");
            if (storage != null) {
                vm.cartList = JSON.parse(storage);
            }
            vm.cartList = mergeArr(JSON.parse(JSON.stringify(vm.cartList)), item);
            console.log(vm.cartList);
            localStorage.setItem("cartList", JSON.stringify(vm.cartList));
        }

        function mergeArr(arr, item) {
            let result = [];
            let idx = null;
            for (let i = 0; i < arr.length; i++) {
                if (arr[i].bookId == item.bookId && arr[i].storeId == item.storeId) {
                    idx = i;
                    break;
                }
            }

            if (idx == null) {
                result = JSON.parse(JSON.stringify(arr));
                result.push(item);
                return result;
            }

            for (let i = 0; i < arr.length; i++) {
                if (i == idx) {
                    let temp = arr[i];
                    temp.quantity += item.quantity;
                    result.push(temp);
                } else {
                    result.push(arr[i]);
                }
            }
            return result;
        }

        $scope.$on('ADD_TO_CART', function (event, response) {
            getCart();
            vm.addToCart(response);
        })

        $scope.$on('RESET', function (event, response) {
            getCart();
        })

        vm.clearList = function () {
            localStorage.clear();
            vm.cartList = [];
        }

        function loadData() {
            vm.books = null;
            BookService.getAll().done(function (response) {
                $scope.$apply(function () {
                    vm.books = response;
                })
            })
        }

        loadData();

        vm.account = {};
        vm.username = "Menu"
        $.ajax({
            url: "/api/account/current",
            type: "GET",
            success: function (res) {
                if (res.data != null){
                    $scope.$apply(function () {
                        if (res.data != null)
                            vm.account = res.data.account;
                        vm.username = vm.account.username;
                        switch (res.data.role) {
                            case "ROLE_CUSTOMER":{
                                vm.username = res.data.customer.name;
                                break;
                            }
                            case "ROLE_EMPLOYEE":{
                                vm.username = res.data.employee.name;
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    })
                }
            }
        })
    }
})();
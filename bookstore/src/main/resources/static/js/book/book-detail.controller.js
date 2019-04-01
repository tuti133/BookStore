(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BookDetailController", BookDetailController)
    BookDetailController.$inject = ["$scope", "$mdDialog", "AccountService", "AlertService", "BookService", "BookStoreService"];

    function BookDetailController($scope, $mdDialog, AccountService, AlertService, BookService, BookStoreService) {
        let vm = this;
        let url = window.location.href
        let arr = url.split("/");
        let bookId = arr[arr.length - 1];
        vm.book = null;

        vm.cartItem = {
            bookId: bookId,
            storeId: null,
            quantity: 1,
            book: null,
        }

        vm.changeStore = changeStore;

        function changeStore() {
            $.ajax({
                url: "/api/bookQuantity/getOneByStore",
                type: "GET",
                data: {
                    bookId: bookId,
                    storeId: vm.cartItem.storeId
                },
                success: function (response) {
                    $scope.$apply(function () {
                        if (response.errorCode == 0) {
                            vm.amount = response.data.quantity;
                        }
                    })
                }
            })
        }

        vm.add = add;

        function add() {
            BookService.addToCart(JSON.parse(JSON.stringify(vm.cartItem)));
        }

        loadData();

        function loadData() {
            $.ajax({
                type: "GET",
                url: "/api/books/" + bookId,
                success: function (e) {
                    $scope.$apply(function () {
                        vm.book = e;
                        vm.cartItem.book = e;
                    })
                }
            })

            BookStoreService.getAll().done(stores => {
                $scope.$apply(function () {
                    vm.stores = stores.data;
                })
            })
        }


    }
})();
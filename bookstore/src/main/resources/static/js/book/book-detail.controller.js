(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BookDetailController", BookDetailController)
    BookDetailController.$inject = ["$scope", "$mdDialog", "AccountService", "AlertService", "BookService", "BookStoreService"];

    function BookDetailController($rootScope, $mdDialog, AccountService, AlertService, BookService, BookStoreService) {
        let vm = this;
        let url = window.location.href
        let arr = url.split("/");
        let bookId = arr[arr.length-1];
        vm.book = null;
        vm.add = add;

        vm.cartItem = {
            bookId: bookId,
            storeId: null,
            quantity: 1,
            book: null,
        }


        function add(){
            console.log(vm.cartItem);
            BookService.addToCart(JSON.parse(JSON.stringify(vm.cartItem)));
        }

        $.ajax({
            type: "GET",
            url: "/api/books/" + bookId,
            success: function (e) {
                vm.book = e;
                vm.cartItem.book = e;
            }
        })

        BookStoreService.getAll().done(stores => {
            vm.stores = stores.data;
        })

    }
})();
(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('BookService', BookService);

    BookService.$inject = ["$rootScope"];

    function BookService($rootScope) {
        let service = {
            get: get,
            getAll: getAll,
            save: save,
            addToCart: addToCart
        }

        return service;

        function get(id) {
            return $.ajax({
                url: "/api/books/" + id ,
                type: "GET",
            })
        }

        function getAll() {
            return $.ajax({
                url: "/api/books",
                type: "GET",
            })
        }
        
        function save(dto) {
            console.log(dto)
            return $.ajax({
                url: "/api/books",
                type: "POST",
                processData: false,
                contentType: false,
                headers: {
                    "Content-Type": undefined
                },
                data: dto,
            })
        }

        function addToCart(book) {
            $rootScope.$broadcast('ADD_TO_CART', book);
        }
    }
})();

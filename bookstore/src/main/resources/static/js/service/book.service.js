(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('BookService', BookService);

    BookService.$inject = [];

    function BookService() {
        let service = {
            get: get,
            getAll: getAll,
            save: save
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
    }
})();

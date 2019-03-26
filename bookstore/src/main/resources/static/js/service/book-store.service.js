(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('BookStoreService', BookStoreService);

    BookStoreService.$inject = [];

    function BookStoreService() {
        let service = {
            get: get,
            getAll: getAll,
            findByEmployee: findByEmployee
        }

        return service;

        function get(id) {
            return $.ajax({
                url: "/api/bookStore/get",
                method: "GET",
                contentType: "application/json; charset:utf-8",
                data: {
                    id: id
                },
            })
        }

        function getAll() {
            return $.ajax({
                url: "/api/bookStore/getAll",
                method: "GET",
                contentType: "application/json; charset:utf-8",
            })
        }
        
        function findByEmployee() {
            
        }
    }
})();

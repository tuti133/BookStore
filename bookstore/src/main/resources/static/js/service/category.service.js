(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('CategoryService', CategoryService);

    CategoryService.$inject = [];

    function CategoryService() {
        let service = {
            getAll: getAll,
            create: create,
            update: update
        }

        return service;

        function getAll() {
            return $.ajax({
                url: "/api/category/getAll",
                method: "GET",
            })
        }

        function create(dto) {
            return $.ajax({
                url: "/api/category/create",
                method: "POST",
                contentType: "application/json; charset:utf-8",
                data: JSON.stringify(dto),
            })
        }

        function update(dto) {
            return $.ajax({
                url: "/api/category/update",
                method: "POST",
                contentType: "application/json; charset:utf-8",
                data: JSON.stringify(dto),
            })
        }
    }
})();

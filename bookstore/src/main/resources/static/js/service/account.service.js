(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('AccountService', AccountService);

    AccountService.$inject = [];

    function AccountService() {
        let service = {
            get: get,
            getAll: getAll,
            current: current,
            create: create,
            update: update
        }

        return service;

        function get(id) {
            return $.ajax({
                url: "/api/account/get",
                method: "GET",
                contentType: "application/json; charset:utf-8",
                data: {
                    id: id
                },
            })
        }

        function getAll() {
            return $.ajax({
                url: "/api/account/getAll",
                method: "GET",
                contentType: "application/json; charset:utf-8",
            })
        }

        function current() {
            return $.ajax({
                url: "/api/account/current",
                method: "GET",
                contentType: "application/json; charset:utf-8",
            })
        }

        function create(dto) {
            return $.ajax({
                url: "/api/account/create",
                method: "POST",
                contentType: "application/json; charset:utf-8",
                data: JSON.stringify(dto),
            })
        }

        function update(dto) {
            return $.ajax({
                url: "/api/account/update",
                method: "POST",
                contentType: "application/json; charset:utf-8",
                data: JSON.stringify(dto),
            })
        }

    }
})();

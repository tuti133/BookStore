(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('BookQuantityService', BookQuantityService);

    BookQuantityService.$inject = [];

    function BookQuantityService() {
        let service = {
            getByStore: getByStore,
            update: update,
        }

        return service;

        function getByStore(bookStoreId) {
            return $.ajax({
                url: "/api/bookQuantity/getByStore",
                method: "GET",
                contentType: "application/json; charset:utf-8",
                data: {
                    bookStoreId: bookStoreId
                }
            })
        }

        function update(bookStore) {
            return $.ajax({
                url: "/api/bookQuantity/update",
                method: "POST",
                contentType: "application/json; charset:utf-8",
                data: JSON.stringify(bookStore),
            })
        }
    }
})();

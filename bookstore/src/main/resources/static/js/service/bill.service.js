(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('BillService', BillService);

    BillService.$inject = [];

    function BillService() {
        let service = {
            getByType: getByType,
            getByCurrentUser: getByCurrentUser,
            update: update
        }
        
        function getByType(type) {
            return $.ajax({
                url: "/api/buy/getByType",
                type: "GET",
                data: {
                    type: type
                }
            })
        }

        function getByCurrentUser() {
            return $.ajax({
                url: "/api/buy/getByCurrentUser",
                type: "GET",
            })
        }

        function update(id, status) {
            return $.ajax({
                url: "/api/buy/update/" + id,
                type: "POST",
                data: {
                    status: status
                }
            })
        }

        return service;

    }
})();

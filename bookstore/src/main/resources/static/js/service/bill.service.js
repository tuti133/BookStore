(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('BillService', BillService);

    BillService.$inject = [];

    function BillService() {
        let service = {
            getByStatusType: getByStatusType,
            getByCurrentUser: getByCurrentUser,
            update: update,
            statistic: statistic
        }

        function statistic(type, from, to, buyStatus) {
            return $.ajax({
                url: "/api/thongke",
                type: "GET",
                data: {
                    type: type,
                    from: from,
                    to: to,
                    buyStatus: buyStatus,
                }
            })
        }
        
        function getByStatusType(type) {
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

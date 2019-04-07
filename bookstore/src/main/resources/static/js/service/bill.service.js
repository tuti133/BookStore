(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('BillService', BillService);

    BillService.$inject = ["$mdDialog"];

    function BillService($mdDialog) {
        let service = {
            getByStatusType: getByStatusType,
            getByCurrentUser: getByCurrentUser,
            update: update,
            statistic: statistic,
            detail: detail,
        }


        function detail(ev, data) {
            $mdDialog.show({
                controller: BillDialogController,
                controllerAs: "vm",
                templateUrl: '/dialog/bill-dialog.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                fullscreen: true,
                resolve: {
                    data: function () {
                        return data;
                    }
                }
            }).then(function (response) {

            }, function (err) {

            });
        };

        function BillDialogController($mdDialog, data) {
            let vm = this;
            vm.data = data;
            vm.cancel = cancel;

            function cancel() {
                $mdDialog.cancel();
            }
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

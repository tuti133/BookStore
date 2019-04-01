(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('AlertService', AlertService);

    AlertService.$inject = ["$mdToast", "$mdDialog"];

    function AlertService($mdToast, $mdDialog) {
        let service = {
            success: success,
            error: error,
            showConfirm: showConfirm,
        }

        return service;

        function success(message, delay, pos) {
            let position = pos == null ? "top right" : pos;

            let toast = $mdToast.simple()
                .textContent(message)
                .toastClass("text-center")
                .theme('success')
                .position(position)
                .hideDelay(delay);
            $mdToast.show(toast).then(function (response) {
            });
        }

        function error(message, delay, pos) {
            let position = pos == null ? "top right" : pos;
            let toast = $mdToast.simple()
                .textContent(message)
                .toastClass("text-center")
                .theme('error')
                .position(position)
                .hideDelay(delay);
            $mdToast.show(toast).then(function (response) {
            });
        }

        function showConfirm(message) {
            let confirm = $mdDialog.confirm()
                .title(message)
                .textContent('')
                .ariaLabel('Confirm')
                .ok('OK')
                .cancel('Hủy');

            return $mdDialog.show(confirm);
        }

    }
})();

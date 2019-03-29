(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('AlertService', AlertService);

    AlertService.$inject = ["$mdToast"];

    function AlertService($mdToast) {
        let service = {
            success: success,
            error: error,
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

    }
})();

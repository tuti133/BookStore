(function () {
    'use strict';
    angular
        .module('BookStoreApp')
        .factory('AlertService', AlertService);

    AlertService.$inject = ["$mdToast"];

    function AlertService($mdToast) {
        let position = "top right";
        let service = {
            setPosition: setPosition,
            success: success,
            error: error,
        }

        return service;

        function setPosition(pos) {
            position = pos;
        }


        function success(message, delay) {
            let toast = $mdToast.simple()
                .textContent(message)
                .toastClass("text-center")
                .theme('success')
                .position(position)
                .hideDelay(delay);
            $mdToast.show(toast).then(function (response) {
            });
        }

        function error(message, delay) {
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

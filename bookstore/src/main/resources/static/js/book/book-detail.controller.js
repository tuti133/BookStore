(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BookDetailController", BookDetailController)
    BookDetailController.$inject = ["$scope", "$mdDialog", "AccountService", "AlertService", "BookService", "BookStoreService"];

    function BookDetailController($scope, $mdDialog, AccountService, AlertService, BookService, BookStoreService) {
        let vm = this;
        let url = window.location.href
        let arr = url.split("/");
        let bookId = arr[arr.length - 1];
        vm.book = null;
        vm.isAuthenticated = false;
        vm.isRated = false;
        vm.currentUserRating = null;
        vm.bookRatings = [];

        vm.cartItem = {
            bookId: bookId,
            storeId: null,
            quantity: 1,
            book: null,
        }

        vm.getRating = function (rate) {
            return rate ? rate : 0;
        }


        function loadRating() {
            AccountService.current().done(function (res) {
                console.log(res);
                vm.isAuthenticated = res.data.role == "ROLE_CUSTOMER";
                if (vm.isAuthenticated) {
                    BookService.getUserRating(bookId).done(function (res) {
                        $scope.$apply(function () {
                            vm.isRated = res.data != null;
                            vm.currentUserRating = res.data;
                            vm.currentUserRating.displayDate = new Date(vm.currentUserRating.createDate);
                        })
                    })
                }
            })

            BookService.getBookRating(bookId).done(function (response) {
                $scope.$apply(function () {
                    vm.bookRatings = response.data;
                    vm.bookRatings.forEach(rate => {
                        rate.displayDate = new Date(rate.createDate);
                    })
                })
            })
        }

        loadData();

        function loadData() {
            loadRating();

            $.ajax({
                type: "GET",
                url: "/api/books/" + bookId,
                success: function (e) {
                    $scope.$apply(function () {
                        console.log(e);
                        vm.book = e;
                        vm.cartItem.book = e;
                    })
                }
            })

            BookStoreService.getAll().done(stores => {
                $scope.$apply(function () {
                    vm.stores = stores.data;
                })
            })


        }

        vm.changeStore = changeStore;

        function changeStore() {
            $.ajax({
                url: "/api/bookQuantity/getOneByStore",
                type: "GET",
                data: {
                    bookId: bookId,
                    storeId: vm.cartItem.storeId
                },
                success: function (response) {
                    $scope.$apply(function () {
                        if (response.errorCode == 0) {
                            vm.amount = response.data.quantity;
                        }
                    })
                }
            })
        }

        // Add to cart
        vm.add = add;

        function add() {
            BookService.addToCart(JSON.parse(JSON.stringify(vm.cartItem)));
        }

        vm.createRating = createRating;

        function createRating() {
            $mdDialog.show({
                controller: RatingDialogController,
                controllerAs: "vm",
                templateUrl: '/dialog/rating-dialog.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: true,
                resolve: {
                    rateDto: function () {
                        return {
                            bookId: bookId,
                            rate: null,
                            comment: null,
                        }
                    }
                }
            }).then(function (response) {
                if (response.errorCode == 0) {
                    AlertService.success(response.message, 2000);
                    loadRating();
                } else AlertService.error(response.message, 2000);
            }, function (err) {

            });
        }


        vm.editRating = editRating;

        function editRating(rateDto) {
            $mdDialog.show({
                controller: RatingDialogController,
                controllerAs: "vm",
                templateUrl: '/dialog/rating-dialog.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: true,
                resolve: {
                    rateDto: function () {
                        return JSON.parse(JSON.stringify(rateDto))
                    }
                }
            }).then(function (response) {
                if (response.errorCode == 0) {
                    AlertService.success(response.message, 2000);
                    loadData();
                } else AlertService.error(response.message, 2000);
            }, function (err) {

            });
        }

        function RatingDialogController($scope, $mdDialog, rateDto) {
            let vm = this;
            vm.rateDto = rateDto;
            console.log(vm.rateDto);
            vm.save = save;
            vm.cancel = cancel;
            vm.changeRate = changeRate;

            function changeRate(e) {
                vm.rateDto.rate = e.rating;
            }

            vm.getRating = function () {
                if (vm.rateDto.rate == null) return '0';
                return vm.rateDto.rate;
            }

            vm.label = function () {
                switch (vm.rateDto.rate) {
                    case 1: {
                        return "Rất tệ";
                        break;
                    }
                    case 2: {
                        return "Tệ";
                        break;
                    }
                    case 3: {
                        return "Bình thường";
                        break;
                    }
                    case 4: {
                        return "Tốt";
                        break;
                    }
                    case 5: {
                        return "Rất tốt";
                        break;
                    }
                    default: {
                        return "";
                        break;
                    }
                }
            }

            function save() {
                vm.isSaving = true;
                console.log(vm.rateDto);
                if (vm.rateDto.book != null) {
                    vm.rateDto.bookId = vm.rateDto.book.id;
                }
                BookService.rating(vm.rateDto).done(function (response) {
                    $mdDialog.hide(response);
                });
            }

            function cancel() {
                $mdDialog.cancel();
            }

        }


        vm.seeReview = seeReview;

        function seeReview() {
            $mdDialog.show({
                controller: ReviewDialogController,
                controllerAs: "vm",
                templateUrl: '/dialog/review-dialog.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: true,
                resolve: {
                    listReview: function () {
                        return JSON.parse(JSON.stringify(vm.bookRatings));
                    }
                }
            }).then(function (response) {
            }, function (err) {
            });
        }

        function ReviewDialogController($scope, $mdDialog, listReview) {
            let vm = this;
            vm.listReview = listReview;
            console.log(listReview);
            vm.cancel = cancel;

            vm.getRating = function (rate) {
                return rate;
            }

            function cancel() {
                $mdDialog.cancel();
            }

        }

    }
})();
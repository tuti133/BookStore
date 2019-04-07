(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("DashboardController", DashboardController)
    DashboardController.$inject = ["$scope", "$mdDialog", "AccountService", "AlertService", "BookService", "BillService"];

    function DashboardController($scope, $mdDialog, AccountService, AlertService, BookService, BillService) {
        let vm = this;

        vm.adminFunctions = [
            {
                name: "Quản lý tài khoản",
                url: "/admin/account",
                icon: "supervisor_account"
            },
            {
                name: "Quản lý sách",
                url: "/admin/book",
                icon: "library_books"
            },
            {
                name: "Quản lý lượng sách",
                url: "/admin/book-quantity",
                icon: "view_weeks"
            },
            {
                name: "Quản lý danh mục",
                url: "/admin/category",
                icon: "list_alt"
            },

            {
                name: "Đổi mật khẩu",
                url: "/password",
                icon: "vpn_key"
            },
            {
                name: "Đăng xuất",
                url: "/logout",
                icon: "exit_to_app"
            },
        ]

        vm.allType = [
            {id: 0, name: "Tất cả"},
            {id: 1, name: "Offline"},
            {id: 2, name: "Online"},
        ]

        vm.allBuyStatus = [
            {id: 0, name: "Tất cả"},
            {id: 1, name: "Đang xử lý"},
            {id: 2, name: "Đang giao hàng"},
            {id: 3, name: "Đã giao hàng"},
            {id: 4, name: "Đã hủy"},
        ]

        vm.from = new Date((new Date()).getTime() - 3600 * 24 * 1000);
        vm.to = new Date();
        vm.type = 0;
        vm.buyStatus = 0;

        vm.statistic = null;
        vm.listBills = null;

        vm.changeDate = changeDate;
        function changeDate() {
            loadCommonData();
            loadBillData();
        }
        
        vm.changeBillType = changeBillType;
        function changeBillType() {
            loadBillData();
        }

        loadBillData();
        loadCommonData();

        function loadCommonData() {
            let fromTemp = new Date(vm.from);
            let toTemp = new Date(vm.to);
            fromTemp.setHours(0);
            fromTemp.setMinutes(0);
            fromTemp.setSeconds(0);
            fromTemp.setMilliseconds(0);

            toTemp.setHours(0);
            toTemp.setMinutes(0);
            toTemp.setSeconds(0);
            toTemp.setMilliseconds(0);

            let from = new Date(fromTemp).getTime();
            let to = new Date(toTemp).getTime();
            to += 3600 * 24 * 1000
            console.log(new Date(from));
            console.log(new Date(to));
            BillService.statistic(0, from, to, 0).done(function (response) {
                $scope.$apply(function () {
                    console.log("Load statistic data: ")
                    console.log(response);
                    vm.statistic = response;
                    console.log(vm.statistic)
                    vm.offlineBill = [];
                    vm.onlineBill = [];
                    vm.proccessing = [];
                    vm.shipping = [];
                    vm.completed = [];
                    vm.canceled = [];
                    response.billDtoList.forEach(function (e) {
                        if (e.type == 1) vm.offlineBill.push(e);
                        else {
                            vm.onlineBill.push(e);
                            switch (e.status) {
                                case '1': {
                                    vm.proccessing.push(e);
                                    break;
                                }
                                case '2': {
                                    vm.shipping.push(e);
                                    break;
                                }
                                case '3': {
                                    vm.completed.push(e);
                                    break;
                                }
                                case '4': {
                                    vm.canceled.push(e);
                                    break;
                                }
                            }
                        }
                    })
                    setTimeout(renderChart, 200);
                })
            })
        }

        function loadBillData() {
            let from = new Date(vm.from).getTime();
            let to = new Date(vm.to).getTime();
            to += 3600 * 24 * 1000;
            if (isNaN(vm.type)) vm.type = 0;
            if (isNaN(vm.buyStatus)) vm.buyStatus = 0;
            BillService.statistic(vm.type, from, to, vm.buyStatus).done(function (response) {
                $scope.$apply(function () {
                    console.log("Load bill data: ")
                    console.log(response);
                    vm.listBills = response.billDtoList;
                })
            })
        }


        function renderChart() {
            // Highcharts.chart('type_chart', {
            //     chart: {
            //         type: 'pie',
            //     },
            //     title: {
            //         text: 'Thống kê loại hóa đơn'
            //     },
            //     series: [{
            //         name: 'Số lượng',
            //         colorByPoint: true,
            //         data: [{
            //             name: 'Online',
            //             y: vm.onlineBill.length,
            //         }, {
            //             name: 'Offline',
            //             y: vm.offlineBill.length
            //         }]
            //     }]
            // });
            //
            // Highcharts.chart('book_chart', {
            //     chart: {
            //         type: 'pie',
            //     },
            //     title: {
            //         text: 'Thống kê bán sách'
            //     },
            //     series: [{
            //         name: 'Số lượng',
            //         colorByPoint: true,
            //         data: [{
            //             name: 'Online',
            //             y: vm.statistic.totalBookOnline,
            //         }, {
            //             name: 'Offline',
            //             y: vm.statistic.totalBookOffline,
            //         }]
            //     }]
            // });

            Highcharts.chart('status_chart', {
                chart: {
                    type: 'column',
                },
                title: {
                    text: 'Thống kê tình trạng hóa đơn online'
                },
                xAxis: {
                    categories: ['Đang xử lý', 'Đang giao hàng', 'Đã giao hàng', 'Đã hủy']
                },
                yAxis: {
                    title: {
                        text: 'Số lượng'
                    }
                },
                series: [{
                    showInLegend: false,
                    name: 'Số lượng',
                    data: [vm.proccessing.length, vm.shipping.length, vm.completed.length, vm.canceled.length]
                }]
            });



            Highcharts.chart('common_chart', {
                chart: {
                    type: 'bar'
                },
                title: {
                    text: 'Thống kê theo hình thức bán hàng'
                },
                xAxis: {
                    categories: ['Doanh thu', 'Sách', 'Hóa đơn']
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Tỉ lệ'
                    }
                },
                tooltip: {
                    pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.0f}%)<br/>',
                    shared: true
                },
                plotOptions: {
                    bar: {
                        stacking: 'percent'
                    }
                },
                series: [{
                    name: 'Online',
                    data: [vm.statistic.totalOnline, vm.statistic.totalBookOnline, vm.onlineBill.length]
                }, {
                    name: 'Offline',
                    data: [vm.statistic.totalOffline, vm.statistic.totalBookOffline, vm.offlineBill.length]
                }]
            });
        }
    }
})();
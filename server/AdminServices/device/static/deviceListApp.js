
$(document).ready(function() {
    var oTable = new TableInit();
    oTable.Init();
});

var TableInit = function () {
    var $customer_table = $('#tb_devices');
    var workshop_code = $('#workshop_code').val();
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $customer_table.bootstrapTable({
            url: '/api/v1/devices/',          //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 20,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: true,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 1,             //最少允许的列数
            clickToSelect: true,               //是否启用点击选中行
            //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                     //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'code',
                title: '设备编码',
                formatter: function(value, row, index){
                    return '<a href=/view/device/detail?code=' + row.code + '>'+value+'</a>';
                }
            }, {
                field: 'name',
                title: '名称',
            }, {
                field: 'model',
                title: '型号',
            }, {
                field: 'system',
                title: '所在系统',
            }, {

                field: 'workshop',
                title: '所在车间',
                formatter: function(value, row, index){
                    if(row.workshop_code != null) {
                        return '<a href=/view/devices/?workshop=' + row.workshop_code + '>'+value+'</a>';
                    } else {
                        return '--';
                    }
                }
            }]
        });
    };
    oTableInit.queryParams = function (params) {
        var temp = {
            workshop: workshop_code,
            page_size: params.limit,
            offset: params.offset,
            pk: params.search
        };
        return temp;
    };
    return oTableInit;
};

$(function () {

    $('#searchBtn').click(function () {
        pageaction();
    });

    $('#exportExcel').click(function () {
        exportExcel();
    });

    //初始化分页
    pageaction();
    var pg = $('.pagination');
    $('#pageSelect').live("change", function () {
        pg.trigger('setPage', [$(this).val() - 1]);
    });
    $("#month").datetimepicker(
        {
            bootcssVer: 3,
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 4,
            endView: 3,
            minView: 3,
            forceParse: 0,
            showMeridian: 1,
            formatDate: "yyyymm",
            format: "yyyymm",
            language: 'zh-CN',
            defaultDate: new Date()
        });
});

//分页的参数设置
var getOpt = function () {
    var opt = {
        items_per_page: 10,	//每页记录数
        num_display_entries: 3, //中间显示的页数个数 默认为10
        current_page: 0,	//当前页
        num_edge_entries: 1, //头尾显示的页数个数 默认为0
        link_to: "javascript:void(0)",
        prev_text: "上页",
        next_text: "下页",
        load_first_page: true,
        show_total_info: true,
        show_first_last: true,
        first_text: "首页",
        last_text: "尾页",
        hasSelect: false,
        callback: pageselectCallback //回调函数
    }
    return opt;
}
//分页开始

var pageaction = function () {
    $.get('./list/' + $("#month").val(), {
    }, function (data) {
        currentPageData = data.content;
        $(".pagination").pagination(data.totalElements, getOpt());
    });
}

var pageselectCallback = function (page_index, jq, size) {
    var html = "";
    if (currentPageData != null) {
        fillData(currentPageData);
        currentPageData = null;
    } else
        $.get('./list/' + $("#month").val(), {
            size: size, page: page_index
        }, function (data) {
            fillData(data.content);
        });
}

//填充分页数据
function fillData(data) {
    var $list = $('#tbodyContent').empty();
    $.each(data, function (k, v) {
        var html = "";
        html += '<tr>' +
            '<td>' + (v.sdate == null ? '' : v.sdate) + '</td>' +
            '<td>' + (v.categoryName == null ? '' : v.categoryName) + '</td>' +
            '<td>' + (v.netWeight == null ? '0' : v.netWeight);
        html += '</td></tr>';
        $list.append($(html));

    });
}




function exportExcel() {
    var u = "./down/" + $("#month").val();
    window.location.href = u

}

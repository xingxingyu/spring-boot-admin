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
var currentPageData = null;
var pageaction = function () {
    $.get('./list?t=' + new Date().getTime(), {
        start: $("#start").val(),
        end: $("#end").val(),
        department: $("#department").val(),
        categoryName: $("#category_name").val()
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
        $.get('./list?t=' + new Date().getTime(), {
            size: size, page: page_index, name: $("#name").val()
        }, function (data) {
            fillData(data.content);
        });
}
//填充分页数据
function fillData(data) {
    var $list = $('#tbodyContent').empty();
    $.each(data, function (k, v) {
        var html = "";
        html += '<tr> ' +
            '<td>' + (v.id == null ? '' : v.id) + '</td>' +
            '<td>' + (v.categoryId == null ? '' : v.categoryId) + '</td>' +
            '<td>' + (v.categoryName == null ? '' : v.categoryName) + '</td>' +
            '<td>' + (v.bachId == null ? '' : v.bachId) + '</td>' +
            '<td>' + (v.netWeight == null ? '' : v.netWeight) + '</td>' +
            '<td>' + (v.tare == null ? '' : v.tare) + '</td>' +
            '<td>' + (v.preTare == null ? '' : v.preTare) + '</td>' +
            '<td>' + (v.up_Date == null ? '' : getSmpFormatDateByLong(v.up_Date, true)) + '</td>' +
            '<td>' + (v.sn == null ? '' : v.sn) + '</td>' +
            '<td>' + (v.ip == null ? '' : v.ip) + '</td>' +
            '<td>' + (v.department == null ? '' : v.department) + '</td>' +
            '<td>' + (v.operator == null ? '' : v.operator) + '</td>' +
            '<td>' + (v.isCheck == null ? '' : v.isCheck) + '</td>' +
            '<td>' + (v.checkDif == null ? '' : v.checkDif.toFixed(2)) + '</td>' +
            '<td>' + (v.checkRs == null ? '' : v.checkRs) + '</td>';
        $list.append($(html));

    });
}
//分页结束
var artdialog;
function showDetail(id) {
    $.get("./" + id, {ts: new Date().getTime()}, function (data) {
        art.dialog({
            lock: true,
            opacity: 0.3,
            title: "查看信息",
            width: '750px',
            height: 'auto',
            left: '50%',
            top: '50%',
            content: data,
            esc: true,
            init: function () {
                artdialog = this;
            },
            close: function () {
                artdialog = null;
            }
        });
    });
}


function closeDialog() {
    artdialog.close();
}

function contains(string, substr, isIgnoreCase) {
    if (isIgnoreCase) {
        string = string.toLowerCase();
        substr = substr.toLowerCase();
    }
    var startChar = substr.substring(0, 1);
    var strLen = substr.length;
    for (var j = 0; j < string.length - strLen + 1; j++) {
        if (string.charAt(j) == startChar) //如果匹配起始字符,开始查找
        {
            if (string.substring(j, j + strLen) == substr) //如果从j开始的字符与str匹配，那ok
            {
                return true;
            }
        }
    }
    return false;
}

function exportExcel() {
    var u = "./exportExcel?start=" + document.getElementById("start").value + "&end=" + document.getElementById('end').value + "&categoryName=" + document.getElementById('category_name').value;
    window.location.href = u
}
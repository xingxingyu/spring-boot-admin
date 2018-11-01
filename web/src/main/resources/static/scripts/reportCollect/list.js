$(function () {

    $('#searchBtn').click(function () {
        pageaction();
    });

    $('#view').click(function () {
        param1();
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
    $("#start,#end").datetimepicker(
        {bootcssVer:3,
            weekStart: 1,
            todayBtn:  1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            forceParse: 0,
            showMeridian: 1,
            format: "yyyy-mm-dd hh:ii",
            language: 'zh-CN'
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
    $.get('./list?t=' + new Date().getTime(), {
        start: $("#start").val(),
        end: $("#end").val()
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
            size: size, page: page_index, start: $("#start").val(), end: $("#end").val()
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
            '<td>' + (v.department == null ? '' : v.department) + '</td>' +
            '<td>' + (v.categoryId == null ? '' : v.categoryId) + '</td>' +
            '<td>' + (v.categoryName == null ? '' : v.categoryName) + '</td>' +
            '<td>' + (v.netWeight == null ? '' : v.netWeight) + '</td>';
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
var departments = null;
function param1() {
    $.get("./department?t=" + new Date().getTime(), {
        start: $("#strat").val(),
        end: $("#end").val()
    }, function (data) {
        departments = data;
        param2();
    })
}

var netweights = null;
function param2() {
    $.get("./netweight?t=" + new Date().getTime(), {
        start: $("#start").val(),
        end: $("#end").val()
    }, function (data) {
        netweights = data;
        view();
    })
}

function view() {
    $.get("./view", function (data) {
        art.dialog({
            lock: true,
            opacity: 0.3,
            title: "图表",
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
function formatter(obj) {

    var temp = obj[2011];
    var max = 0;
    var sum = 0;
    for (var i = 0, l = temp.length; i < l; i++) {

        max = Math.max(max, temp[i].value);
        sum += temp[i].value;

        obj[2011][i] = {
            name: temp[i].name,
            value: temp[i].value
        }
    }
    obj[2011 + 'max'] = Math.floor(max / 100) * 100;
    obj[2011 + 'sum'] = sum;

    return obj;
}

//无/n分割数组
function array() {
    var departmentArray = [];
    for (var i = 0; i < departments.length; i++) {
        departmentArray[i] = departments[i].depar;
    }
    return departmentArray;
}


//有/n分割数组
function array2() {
    var departmentArray = [];
    for (var i = 0; i < departments.length; i++) {
        if (i % 2 == 0) {
            departmentArray[i] = departments[i].depar;
        }
        else {
            departmentArray[i] = "\n" + departments[i].depar;
        }
    }

    return departmentArray;

}

//获取同一种垃圾的不用部门净重列表
function getNetByCategory(x) {
    //x表示传进来的垃圾类型名称
    var res = []
    var key;
    var value;
//取某一类型垃圾的净重数据,数组长度只是有数据部门
    for (var i = 0, len = netweights.length; i < len; i++) {
        if (netweights[i].categoryName == x) {
            name = netweights[i].depar;
            value = netweights[i].netWeight
            res.push({"name": name, "value": value});
        }

    }


    //排序，补全和默认值
    var dept = array()
    var flag = -1
    resnew = []
    var temp

    for (var j = 0, leng = dept.length; j < leng; j++) {
        flag = -1
        for (var i = 0, len = res.length; i < len; i++) {
            if (res[i].name == dept[j]) {
                //保存索引位置
                flag = i
            }
        }
        //如果找不到对应的科室
        if (flag >= 0) {
            //存在就不动，不存在就给默认值
            resnew[j] = res[flag]
        } else {
            temp = dept[j]
            resnew[j] = {'name': temp, 'value': 0}
        }
    }

    return {2011: resnew};
}


function exportExcel() {
    var u = "./exportExcel?start="+ $("#start").val()+"&end="+$("#end").val();
    window.location.href=u

}
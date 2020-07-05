/**
 * 封装通用的分页功能,ajax查询及删除功能
 * @type {Object}
 */
var util = new Object();

util.currentPage = 1; //当前页码
util.num=1;           //序号
util.tableTitle;

/**
 *  初始化分页插件
 * */
util.showPage = function (currentPage, totalPage) {
    if(totalPage==0){
        layer.alert(
            '没有查询到对应的数据',
            {icon: 2, time:1000});
    }
    if (totalPage < 2) {
        $("#page").hide();
        return;
    } else {
        $("#page").show();
    }
    if (currentPage > totalPage) {
        this.currentPage = 1;
        this.query();
        return;
    }
    var numberOfPages = 10;
    if (totalPage < numberOfPages) {
        numberOfPages = totalPage;
    }
    var element = $('#page');
    var options = {
        bootstrapMajorVersion: 3, //bootstrap的版本要求
        alignment: "center",
        currentPage: currentPage,          //设置当前页
        numberOfPages: numberOfPages,		 //设置可以点击到的页数范围
        totalPages: totalPage,            //设置总页数
        itemTexts: function (type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "上一页";
                case "next":
                    return "下一页";
                case "last":
                    return "末页";
                case "page":
                    return page;
            }
        },//点击事件，用于通过Ajax来刷新整个list列表
        onPageClicked: function (event, originalEvent, type, page) {
            util.currentPage = page;
            util.query();
        }
    }
    element.bootstrapPaginator(options);
}


util.getFromData=function(form) {
    var root={}
    var like={};
    var eq={};

    //注入页码和当前页
    root["currentPage"]=$("[name='currentPage']").val();
    root["pageSize"]=$("[name='pageSize']").val();

    //文本框都用模糊查询
    form.find("[type='text']").each(function(){
        var value= $(this).val();      //input 值
        var name= $(this).attr('name');
        if(name.indexOf("Date")!=-1){
            //日期字段用root注入
            root[name]=value;
        }else{
            //其它文本字段注入到like参数中
            like[name]=value;
        }
    });

    //单选框 等值匹配
    form.find("[type='radio']:checked").each(function(){
        var value= $(this).val();      //input 值
        var name= $(this).attr('name');
        eq[name]=value;
    });

    //等值匹配
    form.find("select").each(function(){
        var value= $(this).val();
        var name= $(this).attr('name');
        //页码信息是用select装的,所有排除掉
        if(name!="pageSize"){
            eq[name]=value;
        }
    });

    root['eq']=eq;
    root['like']=like;
    return root;
}

/**
 * ajax查询后,刷新表格样式
 */
util.tableInit = function () {
    $("table tr:first").css("background-color", "#F5FAFE");
    $('table tr').hover(
        function () {
            $(this).addClass("trHover");
        },
        function () {
            $(this).removeClass("trHover")
        }
    );
}


/**
 * 判断字符串是否为空
 * @param data
 * @returns {boolean}
 */
util.checkIsNull = function (data) {
    return data == undefined || data == "undefined" || data == null || data == "null" || data == '' || data.length == 0;
}


function checkAll() {
    var checked = document.getElementById("firstCheck").checked;
    $("table tr td input").each(function () {
        if ($(this).is(":visible")) {
            this.checked = checked;
        } else {
            this.checked = false;
        }
    })
}


//用户作为查询的标示
util.ajaxing=false;

/**
 * 通用查询 禁止用户频繁点击查询请求,只有等回调函数执行完,才能触发下一次请求
 * @param fn  成功后的回调函数
 */
util.query = function () {
    if(this.ajaxing){
       return;
    }
    this.ajaxing=true;
    layer.closeAll('dialog');
    layer.load();
    var $form=$("form");
    var url= $form.attr("action");
    $("[name='currentPage']").val(util.currentPage);
    this.num = (this.currentPage - 1) * parseInt($("[name='pageSize']").val());
    console.log(JSON.stringify(util.getFromData($form)))
    $.ajax({
        type: 'post',
        url: url,
        data: JSON.stringify(util.getFromData($form)),
        contentType: "application/json",
        async:false, //把方法设置为同步查询
        success: querySuccess,
        error:function () {
            layer.msg("网络异常!", {icon: 2, time: 2000});
        }
    });
    layer.closeAll('loading');
    document.getElementById("firstCheck").addEventListener("click", checkAll, false)
    this.ajaxing=false;
    this.tableInit();
}

/**
 *  删除一条数据
 * @param url
 */
util.delete = function (url) {
    layer.confirm('确认要删除吗？', function (index) {
        layer.closeAll('dialog');
        layer.load();
        $.ajax({
            type: 'DELETE',
            url: url,
            success: function (result) {
                layer.closeAll('loading');
                if (result) {
                    util.currentPage=1;
                    util.query();
                    layer.msg("删除成功", {icon: 1, time: 1500});
                } else {
                    layer.msg(result.msg, {icon: 2, time: 1500});
                }
            },error:function () {
                layer.msg('网络异常!', {icon: 2, time: 2000});
            }
        });
    })
}


/**
 * 根据选中的删除数据
 * @param url 请求路径
 */
util.deleteAll = function (url) {
    var idStr = "";
    $("input:checkbox[name='selected']:checked").each(function (i) {
        var val = $(this).val();
        idStr += val + ",";
    });
    if (idStr == "") {
        layer.alert(
            "您未选中任何数据!",
            {icon: 2, time: 2000}
        );
        return false;
    } else {
        if (idStr.indexOf(",") != -1) {
            idStr = idStr.substring(0, idStr.length - 1);
        }
        url+=idStr;
        layer.confirm('确认要删除吗？', function (index) {
            layer.closeAll('dialog');
            layer.load();
            $.ajax({
                type: 'DELETE',
                url: url,
                success: function (res) {
                    layer.closeAll('loading');
                    if (res) {
                        util.currentPage=1;
                        util.query();
                        layer.msg('删除成功!', {icon: 1, time: 1000});
                    } else {
                        layer.msg(res.msg, {icon: 2, time: 2000});
                    }
                },error:function () {
                    layer.msg('网络异常!', {icon: 2, time: 2000});
                }
            });
        });
    }
}



$(function () {
    $(document).bind("contextmenu", function (e) {
        return false;
    });
    var pageSize=new Array(10,15,30,50,100);
    var html="";
    pageSize.forEach(function (value) {
        html+="<option value='"+value+"'>"+value+"</option>"
    })
    $("[name='pageSize']").html(html);
    $("[name='pageSize']").change(function () {
        util.currentPage = 1;
        util.query();
    });
    $("#firstCheck").change(function () {
        checkAll();
    });
    $(".selectpicker").change(function () {
        $(this).parent().removeClass("open");
    });

})
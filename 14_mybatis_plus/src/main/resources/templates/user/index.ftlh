<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="stylesheet" type="text/css" href="/style/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/style/global.css"/>
    <link rel="stylesheet" type="text/css" href="/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="/lib/Hui-iconfont/1.0.8/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="/h-ui.admin/css/style.css"/>
    <title>用户列表页面</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            list-style: none;
        }

        a {
            text-decoration: none;
        }

        a {
            color: #0f9ae0
        }

        a:hover {
            text-decoration: none;
        }
    </style>

</head>
<body>
<nav class="breadcrumb">
    <i class="Hui-iconfont">&#xe67f;</i> 位置 <span class="c-gray en">&gt;</span><span>用户管理</span><span
            class="c-gray en">&gt;</span><span>用户列表</span> <a
            class="btn btn-success radius "
            style="line-height: 1.6em; margin-top: 3px"
            href="javascript:location.replace(location.href);" title="刷新"><i
                class="Hui-iconfont">&#xe68f;</i></a>
</nav>
<div class="page-container">
    <form action="/user/json">
        <input name="currentPage" type="hidden"/>
        <div class="text-c">
            日期范围： <input type="text"
                         onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}' })"
                         id="datemin" class="input-text Wdate" name="beginDate" style="width: 120px;">
            - <input type="text"
                     onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d' })"
                     id="datemax" name="endDate" class="input-text Wdate" style="width: 120px;">
            年纪: <select name="grade">
                <option value="">请选择</option>
                <option value="7">七年级</option>
                <option value="8">八年纪</option>
                <option value="9">九年级</option>
            </select>
            用户名: <input type="text" class="input-text" style="width: 250px"
                        placeholder="输入用户名" name="username">
            <button type="button" class="btn btn-success radius" onclick="util.query()">
                <i class="Hui-iconfont">&#xe665;</i> 搜索
            </button>
        </div>
        <div class="cl pd-5 bg-1 bk-gray mt-20">
			<span class="l">
                <a id="add" href="javascript:;" class="btn btn-primary radius" onclick=""><i class="Hui-iconfont">&#xe600;</i>添加用户</a>
                <a href="javascript:;" id="delAll" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i>批量删除</a>
            </span>
            <span class="r">
                <select name="pageSize"></select>
            一共有数据：<strong id="recordCount">${page.totalRecord}</strong> 条
			</span>
        </div>
    </form>
    <div class="mt-20">
        <table class="table table-border table-bordered table-hover table-bg" id="dataTable">
            <thead>
            <tr class="text-c">
                <th width="25"><input type="checkbox" id="firstCheck"></th>
                <th width="50">序号</th>
                <th >用户名</th>
                <th >密码</th>
                <th>年级</th>
                <th >创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list page.records as user>
                <tr class="text-c">
                    <td><input type="checkbox" value="${user.id}" name="selected"></td>
                    <td> ${user_index+1}</td>
                    <td>${user.username}</td>
                    <td>${user.password}</td>
                    <td>${user.grade}</td>
                    <td>${user.createTime?string('yyyy-MM-dd hh:mm:ss')}</td>
                    <td><a href="javascript:;;" onclick="save('${user.id}')">修改</a> <a href="javascript:;;"
                                                          onclick="util.delete('/user/${user.id}')">删除</a></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>
<input type="hidden" value="${page.totalPage}" id="count">
<!--分页 -->
<ul id="page"></ul>
</body>

<script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script src="/js/bootstrap-paginator.js"></script>
<script src="/js/common.js"></script>
<script>
    $(function () {
        //初始化加载分业插件
        util.showPage(1, $("#count").val());
        util.tableTitle = $("#dataTable").find("thead").html()
        $("#delAll").click(()=>{util.deleteAll("/user/")});
        $("#add").click(()=>{
            var w = 460;
            var h = 280;
            var title = '添加用户';
            var url = '/user/-1';
            layer_show(title, url, w, h);
        });
    });

    function save(id) {
        var w = 460;
        var h = 280;
        var title = '修改用户';
        var url = '/user/'+id;
        layer_show(title, url, w, h);
    }

    /**
     * 查询成功的回调函数
     */
    function querySuccess(page) {
        var InnerHtml = util.tableTitle;
        page.records.forEach(function (user) {
            util.num++;
            InnerHtml += '<tr class="text-c" >' +
                '<td><input type="checkbox"value="' + user.id + '" name="selected"></td>' +
                '<td >' + util.num + '</td>' +
                '<td>' + user.username + '</td>' +
                '<td>' + user.password + '</td>' +
                '<td>' + user.grade + '</td>' +
                '<td>' + user.createTime + '</td>' +
                '<td><a href="javascript:;;" onclick="save('+user.id+')">修改</a> <a  href="javascript:;;" onclick=util.delete("/user/' + user.id + '")>删除</a></td>' +
                '</tr>';
        });
        $("#dataTable").html(InnerHtml);
        $("#recordCount").html(page.totalRecord);
        util.showPage(util.currentPage, page.totalPage);
    }

</script>
</html>
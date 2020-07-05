<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="Bookmark" href="/favicon.ico">
    <link rel="Shortcut Icon" href="/favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="/lib/Hui-iconfont/1.0.8/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="/h-ui.admin/css/style.css"/>

    <title>更新用户</title>
</head>
<body>
<article class="page-container">
    <form action="" method="post" class="form form-horizontal" id="form-save" enctype="multipart/form-data">
        <input type="hidden" class="input-text" value="${(user.id)!''}"
               name="id">
        <input type="hidden" class="input-text" value="${(user.createTime?string('yyyy-MM-dd hh:mm:ss'))!''}"
               name="createTime">
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class=c-red>*</span>用户名：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text notEmtry" value="${(user.username)!''}" placeholder=""
                       name="username">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class=c-red>*</span>用户密码</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text checkCn" value="${(user.password)!''}" placeholder="" name="password">
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class=c-red>*</span>年级</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text checkCn" value="${(user.grade)!''}" placeholder="" name="grade">
            </div>
        </div>

        <div class="row cl">
            <div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
                <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
            </div>
        </div>
    </form>
</article>

<script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/validate-methods.js"></script>
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/messages_zh.js"></script>
<script src="/js/common.js"></script>
<script>
    $("#form-save").validate({
        rules: {
            username: {
                required: true,
                minlength: 6,
                maxlength: 16
            },
            password: {
                required: true,
                minlength: 6,
                maxlength: 16
            }

        },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            var isAdd = $("[name='id']").val().trim() == "" ? true : false;
            var form = new FormData(document.getElementById("form-save"));
            var url = "/user/";
            var type=isAdd?'POST':'PUT';
            $.ajax({
                type: type,
                url: url,
                data: form,
                processData: false,
                contentType: false,
                success: function (res) {
                    var msg;
                    if (res) {
                        if (isAdd) {
                            msg = "添加成功!";
                        } else {
                            msg = '修改成功!';
                        }
                        layer.msg(msg, {icon: 1, time: 1500}, function () {
                            window.parent.util.query();
                            layer_close();
                        });
                    } else {
                        layer.msg('操作失败!', {icon: 2, time: 2000});
                    }
                },error:function () {
                    layer.msg('网络异常!', {icon: 2, time: 2000});
                }
            })
        }
    });
</script>
</body>
</html>
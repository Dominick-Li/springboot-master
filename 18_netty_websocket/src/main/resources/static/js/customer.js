/**
 * qqbqPath          表情包存放路径
 * openNewsIng    防止鼠标点击左侧切换聊天窗口的频率的锁
 * sendMessage    发送消息推送给websocket
 * openNews       消息列表左侧置顶
 * onmessage      接受来自服务端推送的消息,并显示在页面
 * replace_em     把表情内容替换成对应的img图片
 * refreshMsgBody 让最新的聊天消息一直处于可见
 * searchMail     模糊查询好友信息
 * createMsgBody  构建聊天内容主题
 * */
var chat = {
    qqbqPath: "/qqbq/arclist/",
    openNewsIng: false,
    createMsgBody: function (jsonData) {
        //创建左边的信息
        let li = `<li class="user_item"   id=` + jsonData.sendId + `>` +
            `<div class="user_head"><img src=` + jsonData.headImg + `/></div>` +
            `<div class="user_text">` +
            `<div class="user_name">` + jsonData.sendId + `</div>` +
            `<div class="user_message"></div>` +
            `</div>` +
            `<div class="user_time"></div>` +
            `</li>`;
        $("#user_list").append(li);
        //创建右边的消息主题
        let body = `<ul  class="content" id="body_` + jsonData.sendId + `">` +
            `</ul>`;
        $("#chatbox").append(body);
    },
    sendMessage: function () {
        if ($('#input_box font').length > 0) {
            $('#input_box font').before($('#input_box font').text())
            $('#input_box font').remove()
        }
        if ($('#input_box span').length > 0) {
            $('#input_box span').before($('#input_box span').text())
            $('#input_box span').remove()
        }
        if ($('#input_box pre').length > 0) {
            $('#input_box pre').before($('#input_box pre').text())
            $('#input_box pre').remove()
        }
        var text = $("#input_box").html(); //发送的内容
        if (text == "") {
            alert('不能发送空消息');
            return;
        }
        var liHtml = '<li class="me"><img src="' + $("#myHeadImg").val() + '"><div contenteditable="true" class="editDiv">' + text + '</div></li>';
        $(".windowBody_active").append(liHtml);
        let id = $(".windowBody_active").attr("id");
        var acceptId = id.substring(id.indexOf("_") + 1);//消息接受者的身份id
        if (acceptId == undefined) {
            return;
        }
        this.refreshMsgBody();
        if (!window.WebSocket) {
            return;
        }
        $("#input_box").html("");
        if (websocket.readyState == WebSocket.OPEN) {
            var data = {};
            data.acceptId = acceptId
            data.message = text;
            data.sendId = $("#userId").val();
            data.headImg = $("#myHeadImg").val();
            websocket.send(JSON.stringify(data));
            var $li = $("#" + acceptId);
            $li.find(".user_message").html(text);
            $li.find(".user_time").html(getHourTime());
        } else {
            alert("和服务器连接异常！");
        }
    },
    openNews: function (obj) {
        try {
            if (this.openNewsIng) {
                console.log("return ....")
                return;
            }
            this.openNewsIng = true;
            $(".windowBody_active").removeClass("windowBody_active");
            var id = obj.attr("id");
            $("#body_" + id).addClass("windowBody_active");
            $("#window-firendName").text(id);
            //把当前消息放到首发位
            $(".user_list").prepend(obj);
            $(".user_active").removeClass("user_active");
            obj.on("click", function () {
                chat.openNews($(this));
                if($(".title").css('display')!="none"){
                    $("#phonelist").hide();
                    $(".talk_window").show();
                }
            });
            obj.addClass("user_active");
            this.refreshMsgBody();
            this.openNewsIng = false;
        } catch (e) {
            console.log(e);
            //释放锁
            this.openNewsIng = false;
        }
    },
    onmessage: function (jsonData) {
        var id = "#" + jsonData.sendId;
        //如果消息窗口不存在,则构建聊天窗口
        if ($(id).length == 0) {
            this.createMsgBody(jsonData);
        }
        var $userlist = $(id)
        $userlist.find(".user_message").html(jsonData.message);
        $userlist.find(".user_time").html(getHourTime());
        this.openNews($userlist);
        var liHtml = '<li class="other"><img src="' + jsonData.headImg + '"><div contenteditable="true" class="editDiv">' + jsonData.message + '</div></li>';
        $("#body_"+$(id).attr("id")).append(liHtml);
        //startMusic()
    },
    refreshMsgBody: function () {
        var chatbox = $("#chatbox").css("height");
        //获取网页的聊天主体的可见高度
        var chatboxHeight = parseInt(chatbox.substring(0, chatbox.indexOf("p")));
        var windowBody_active = $(".windowBody_active").css("height");
        var windowBody_height = parseInt(windowBody_active.substring(0, windowBody_active.indexOf("p")));
        $(".windowBody_active").css("top", "-" + (windowBody_height - chatboxHeight) + "px");
    },
    searchMail: function (obj) {
        var condition = obj.value;
        if (obj.value == "") {
            $("#user_list").find(".user_item").show();
        }
        var usernames = $("#user_list").find(".user_name");
        usernames.each(function () {
            if (this.innerText.indexOf(condition) == -1) {
                $(this).parents(".user_item ").hide();
            } else {
                $(this).parents(".user_item ").show();
            }
        });
    }
}

function startMusic() {
    var audio = document.getElementById('music');
    audio.currentTime = 0;
    if (audio !== null) {
        //检测播放是否已暂停.audio.paused 在播放器播放时返回false.
        if (audio.paused) {
            audio.play();//audio.play();// 这个就是播放
        } else {
            audio.pause();// 这个就是暂停
        }
    }
}

function getHourTime() {
    var date = new Date();
    var hh = date.getHours().toString();
    var nn = date.getMinutes().toString();
    return hh + ":" + nn;
}

document.onkeydown = keyDownSearch;

function keyDownSearch(e) {
    // 兼容FF和IE和Opera    
    var theEvent = e || window.event;
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    // 13 代表 回车键
    if (code == 13) {
        // 要执行的函数 或者点击事件
        chat.sendMessage();
        return false;
    }
    return true;
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    if (websocket != null) {
        websocket.close();
    }
};

$(function () {
    $(".editDiv").on("keypress", function (e) {
        e.preventDefault();
    });

    $("#input_box").keyup(function () {
        if ($('#input_box font').length > 0) {
            $('#input_box font').before($('#input_box font').text())
            $('#input_box font').remove()
        }
        if ($('#input_box span').length > 0) {
            $('#input_box span').before($('#input_box span').text())
            $('#input_box span').remove()
        }
        if ($('#input_box pre').length > 0) {
            $('#input_box pre').before($('#input_box pre').text())
            $('#input_box pre').remove()
        }
        // 获取选定对象
        var selection = getSelection()
        // 设置最后光标对象
        lastEditRange = selection.getRangeAt(0)
    });

    $(".office_text").panel({iWheelStep: 32});

    $(".windowSearch").keyup(function () {
        chat.searchMail(this);
    });

    $('.emotion').qqFace({
        id: 'facebox',
        assign: 'input_box',
        path: chat.qqbqPath	//表情存放的路径
    });


    $(".sidestrip_icon a").click(function () {
        $(".sidestrip_icon a").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
        $(".middle").hide().eq($(this).index()).show();
    });
    $("#input_box").hover(() => {
        $('.windows_input').css('background', '#fff');
        $('#input_box').css('background', '#fff');
    }, () => {
        $('.windows_input').css('background', '');
        $('#input_box').css('background', '');
    });
    $(".user_item").click(function () {
        // $(this).siblings(":hidden").show();
        var index = [].indexOf.call(this.parentNode.querySelectorAll(this.tagName), this);
        if (index == 0 && $(".title").css('display')=="none") {
            //如果已经是第一个元素,则不做置顶操作
            alert("已经是第一个元素了")
            return;
        }
        chat.openNews($(this))
        if($(".title").css('display')!="none"){
            $("#phonelist").hide();
            $(".talk_window").show();
        }
    });
    $("#send").click(function () {
        chat.sendMessage();
    });

    $(".back").click(function () {
        $(".talk_window").hide();
        $("#phonelist").show()
    });
});
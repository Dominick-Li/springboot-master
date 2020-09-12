/********************************************websocket代码开始********************************************************/
/**
 * 使用8位随机数作为游客的唯一ID
 * @returns {string}
 */
function randomUserID() {
    return Math.floor(Math.random() * 100000000).toString();
}

var userId = randomUserID();
var userImg = "/images/head/2.jpg";
var websocket;
if (!window.WebSocket) {
    window.WebSocket = window.MozWebSocket;
}
if (window.WebSocket) {
    websocket = new WebSocket(document.getElementById("ws").value + "?userId=" + userId);
    websocket.onmessage = function (event) {
        var json = JSON.parse(event.data);
        chat.onmessage(json)
    };
    websocket.onopen = function (event) {
        console.log('Netty-WebSocket服务器。。。。。。连接')
    };
    websocket.onclose = function (event) {
        console.log("Netty-WebSocket服务器。。。。。。关闭")
    };
} else {
    alert("您的浏览器不支持WebSocket协议！");
}

/********************************************websocket代码结束********************************************************/

/**
 * qqbqPath          表情包存放路径
 * onmessage      接受来自服务端推送的消息,并显示在页面
 * replace_em     把表情内容替换成对应的img图片
 * refreshMsgBody 让最新的聊天消息一直处于可见
 * */
var chat = {
    qqbqPath: "/qqbq/arclist/",
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
        var liHtml = `<li class="me">` +
            `<p> <img src=` + userImg + `/>` + getHourTime() + `&nbsp;&nbsp;&nbsp;&nbsp;<strong>我&nbsp;&nbsp;</strong></p>` +
            `<div contenteditable="true" class="editDiv">` + text + `</div></li>`;
        $(".content").append(liHtml);
        this.refreshMsgBody();
        if (!window.WebSocket) {
            return;
        }
        $("#input_box").html("");
        if (websocket.readyState == WebSocket.OPEN) {
            var data = {};
            data.acceptId = "admin";
            data.message = text;
            data.sendId = userId
            data.headImg = userImg;
            websocket.send(JSON.stringify(data));
        } else {
            alert("和服务器连接异常！");
        }
    },
    onmessage: function (jsonData) {
        var liHtml = `<li class="other">` +
            `<p> <img src=` + jsonData.headImg + ` /><strong>客服经理</strong>&nbsp;&nbsp;&nbsp;&nbsp;` + getHourTime() + `</p>` +
        `<div contenteditable="true" class="editDiv">` + jsonData.message + `</div></li>`;
        $(".content").append(liHtml);
        this.refreshMsgBody();
    },
    refreshMsgBody: function () {
        var chatbox = $("#chatbox").css("height");
        //获取网页的聊天主体的可见高度
        var chatboxHeight = parseInt(chatbox.substring(0, chatbox.indexOf("p")));
        var windowBody_active = $(".content").css("height");
        var windowBody_height = parseInt(windowBody_active.substring(0, windowBody_active.indexOf("p")));
        $(".content").css("top", "-" + (windowBody_height - chatboxHeight) + "px");
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

    $('.emotion').qqFace({
        id: 'facebox',
        assign: 'input_box',
        path: chat.qqbqPath	//表情存放的路径
    });
    //移动端打电话
    $("#phone").click(function () {
        document.getElementById("telA").click();
    });
    $('#doc-dropdown-js').dropdown({justify: '#doc-dropdown-justify-js'});

    $(".office_text").panel({iWheelStep: 32});

    $(".sidestrip_icon a").click(function () {
        $(".sidestrip_icon a").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
        $(".middle").hide().eq($(this).index()).show();
    });
    $("#input_box").focus(function () {
        $('.windows_input').css('background', '#fff');
        $('#input_box').css('background', '#fff');
    });
    $("#input_box").blur(function () {
        $('.windows_input').css('background', '');
        $('#input_box').css('background', '');
    });

    // $("#input_box").hover(() => {
    //     $('.windows_input').css('background', '#fff');
    //     $('#input_box').css('background', '#fff');
    // }, () => {
    //     $('.windows_input').css('background', '');
    //     $('#input_box').css('background', '');
    // });

    $("#send").click(function () {
        chat.sendMessage();
    });
});

























































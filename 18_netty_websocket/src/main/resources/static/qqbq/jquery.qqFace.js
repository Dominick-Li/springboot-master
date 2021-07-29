// QQ表情插件
(function ($) {
    $.fn.qqFace = function (options) {
        var defaults = {
            id: 'facebox',
            path: 'face/',
            assign: 'content',
            tip: 'em_'
        };
        var option = $.extend(defaults, options);
        var assign = $('#' + option.assign);
        var id = option.id;
        var path = option.path;
        var tip = option.tip;

        if (assign.length <= 0) {
            alert('缺少表情赋值对象。');
            return false;
        }

        $(this).click(function (e) {
            var strFace, labFace, imgFace;
            if ($('#' + id).length <= 0) {
                strFace = '<div id="' + id + '" style="position:absolute;display:none;z-index:1000;background-color: #fff;border-radis:6px;padding: 7px" class="qqFace">' +
                    '<table border="0" cellspacing="0" cellpadding="0"><tr>';
                for (var i = 1; i <= 75; i++) {
                    // if(i==36){
                    //     continue;
                    // }
                    labFace = '[' + tip + i + ']';
                    imgFace = `<img src=` + path + i + `.gif>`;
                    strFace += '<td><img src="' + path + i + '.gif" onclick="$(\'#' + option.assign + '\').insertAtCaret(\'' + imgFace + '\');" /></td>';
                    if (i % 15 == 0) strFace += '</tr><tr>';
                }
                strFace += '</tr></table></div>';
            }
            $(this).parent().append(strFace);
            var offset = $(this).position();
            var top = offset.top + $(this).outerHeight();
            console.log(top);
            $('#' + id).css('top', top - 156);
            $('#' + id).css('left', offset.left - 20);
            $('#' + id).show();
            e.stopPropagation();
        });

        $(document).click(function () {
            $('#' + id).hide();
            $('#' + id).remove();
        });
    };

})(jQuery);

jQuery.extend({
    unselectContents: function () {
        if (window.getSelection)
            window.getSelection().removeAllRanges();
        else if (document.selection)
            document.selection.empty();
    }
});
jQuery.fn.extend({
    selectContents: function () {
        $(this).each(function (i) {
            var node = this;
            var selection, range, doc, win;
            if ((doc = node.ownerDocument) && (win = doc.defaultView) && typeof win.getSelection != 'undefined' && typeof doc.createRange != 'undefined' && (selection = window.getSelection()) && typeof selection.removeAllRanges != 'undefined') {
                range = doc.createRange();
                range.selectNode(node);
                if (i == 0) {
                    selection.removeAllRanges();
                }
                selection.addRange(range);
            } else if (document.body && typeof document.body.createTextRange != 'undefined' && (range = document.body.createTextRange())) {
                range.moveToElementText(node);
                range.select();
            }
        });
    },
    set_focus: function () {
        el = $(this).get(0);
        el.focus();
        if ($.support.msie) {
            var range = document.selection.createRange();
            this.last = range;
            range.moveToElementText(el);
            range.select();
            document.selection.empty(); //取消选中
        } else {
            var range = document.createRange();
            range.selectNodeContents(el);
            range.collapse(false);
            var sel = window.getSelection();
            sel.removeAllRanges();
            sel.addRange(range);
        }
    },

    insertAtCaret: function (textFeildValue) {
        var textObj = $(this).get(0);
        if (document.all && textObj.createTextRange && textObj.caretPos) {
            var caretPos = textObj.caretPos;
            caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == '' ?
                textFeildValue + '' : textFeildValue;
        } else if (textObj.setSelectionRange) {
            var rangeStart = textObj.selectionStart;
            var rangeEnd = textObj.selectionEnd;
            var tempStr1 = textObj.value.substring(0, rangeStart);
            var tempStr2 = textObj.value.substring(rangeEnd);
            textObj.innerHTML = tempStr1 + textFeildValue + tempStr2;
            textObj.focus();
            var len = textFeildValue.length;
            textObj.setSelectionRange(rangeStart + len, rangeStart + len);
            textObj.blur();
        } else {
            textObj.innerHTML += textFeildValue;
        }
        this.set_focus();
    }
});
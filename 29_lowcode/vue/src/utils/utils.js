if (typeof String.prototype.startsWith !== 'function') {
    Window.String.prototype.startsWith = function(prefix) {
        return this.slice(0, prefix.length) === prefix;
    };
}

export default {
    serverUrl(apiName) {
        return `/api/${apiName}`;
    },

    serverUrlMock(apiName) {
        return `/${apiName}`;
    },
    /*函数去抖 */
    throttle(fn,wait){
        let lastTime = 0;//记录过去的时间
        return function(){
            let nowTime = new Date().getTime(); //获取时间戳
            if(nowTime - lastTime > wait){
                //用时间戳记录当前时间，当前时间 减去 上一次的时间，如果大于 wait(你设置的 1000毫秒) 说明 过去1000毫秒了，
                //用户可以点击第二次了。
                fn.apply(this, arguments)//执行 主要函数， 但是此时的 fn函数的this指向window，也
                //没有事件源对象， apply改变this 指向oBtn，传入 事件源 arguments[0]  (e)
                lastTime = nowTime;//主要函数执行后， 当前时间就成了过去的时间了。
            }
        }
    },
    query(search) {
        let str = search || window.location.search;
        let objURL = {};

        str.replace(
            new RegExp('([^?=&]+)(=([^&]*))?', 'g'),
            ($0, $1, $2, $3) => {
                objURL[$1] = $3;
            }
        );
        return objURL;
    },

    queryString(url, query) {
        let str = [];
        for (let key in query) {
            if (query[key] !== undefined && query[key] !== null) {
                str.push(key + '=' + query[key]);
            }
        }
        let paramStr = str.join('&');
        return paramStr ? `${url}${url.indexOf('?') > -1 ? '&' : '?'}${paramStr}` : url;
    },getUrlParam(name){
        let url = window.location.search;
        let paramStr = url.substring(url.indexOf("?") + 1, url.length);
        let paramArray = paramStr.split("&");
        let value = "";
        if(this.isEmpty(paramArray)){
            return  value;
        }
        for (let i = 0;   i < paramArray.length; i++) {
            let paramKeyValue = paramArray[i];
            if (paramKeyValue.indexOf(name) == 0) {
                value = paramKeyValue.substring(name.length + 1, paramKeyValue.length);
                break;
            }
        }
        return value;
    },isEmpty(data){
        return data == undefined || data == "undefined" || data == null || data == "null" || data == '' || data.length == 0;
    },
    /* -----------------------------localStorage------------------------------------ */
    /*
   * set localStorage
   */
    setStorage(name, content) {
        if (!name) return;
        if (typeof content !== 'string') {
            content = JSON.stringify(content);
        }
        window.localStorage.setItem(name, content);
    },

    /**
     * get localStorage
     */
    getStorage(name) {
        if (!name) return;
        let content = window.localStorage.getItem(name);
        if (content && content !== 'undefined' && content !== 'null') {
            if (content.startsWith('{') || content.startsWith('[')) {
                content = JSON.parse(content);
            }
        } else {
            content = null;
        }
        return content;
    },

    /**
     * delete localStorage
     */
    removeStorage(name) {
        if (!name) return;
        window.localStorage.removeItem(name);
    },

    /*
   * set sessionStorage
   */
    setSessionStorage(name, content) {
        if (!name) return;
        if (typeof content !== 'string') {
            content = JSON.stringify(content);
        }
        window.sessionStorage.setItem(name, content);
    },
    /**
     * get sessionStorage
     */
    getSessionStorage(name) {
        if (!name) return;
        let content = window.sessionStorage.getItem(name);
        if (content && content !== 'undefined' && content !== 'null') {
            if (content.startsWith('{') || content.startsWith('[')) {
                content = JSON.parse(content);
            }
        } else {
            content = null;
        }
        return content;
    },
    /**
     * delete localStorage
     */
    removeSessionStorage(name) {
        if (!name) return;
        window.sessionStorage.removeItem(name);
    },
    /** 
     * clearSession
    */ 
    clearSessionStorage() {
        window.sessionStorage.clear();
    }
};

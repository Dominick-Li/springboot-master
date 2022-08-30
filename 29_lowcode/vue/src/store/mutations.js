export default {
    // 菜单menu
    // 角色动态改变不变的tab
    updateBegin(state,beginMenuItem){
        state.editableTabs.push(beginMenuItem)
    },
    // 添加tabs
    add_tabs (state, data) {
        console.log('add_tabs----',data)
        if(data.path&&data.name&&data.title){
            state.editableTabs.push(data);
        }
    },
    // 刷新重置tabs
    refresh_tabs (state, data) {
        state.editableTabs=data
    },
    // 删除tabs
    delete_tabs (state, route) {
        var index = 0;
        for (var option of state.editableTabs) {
            if (option.path == route) {
                break;
            }
            index++;
        }
        state.editableTabs.splice(index, 1);
    },
    // 设置当前激活的tab
    set_active_index (state, index) {
        state.editableTabsValue = index;
    },
    // 是否是要新开fab页
    changeTab(state,newTabBool){
        state.isTab=newTabBool
    },
    // 还原tab数组
    clearTabs(state){
        state.editableTabs=[]
        state.editableTabsValue="",
        state.isTab= true
    }, 
    setEachCondition(state,newVal){
        state.eachCondition[newVal.name]=newVal.condition
    },
    clearEachCondition(state,name){
        delete state.eachCondition[name]
    },
    clearStore(state){
        state.editableTabsValue= ""
        state.editableTabs= []
        state.isTab=true
        state.eachCondition={}
        state.addRoutes=[]    // 动态路由添加
    },

    setAddRoutes(state,list){
        state.addRoutes=list
    }
}
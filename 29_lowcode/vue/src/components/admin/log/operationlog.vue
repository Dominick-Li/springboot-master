<template>
    <div >
        <el-container v-if="refreshView">
            <el-main>
                <div>
                    <el-form label-width="80px">
                        <el-row >
                            <el-col :span="6">
                              <el-form-item label="操作员" class="names">
                                <el-input v-model="condition.like.username"></el-input>
                            </el-form-item>
                            </el-col>
                              <el-col :span="4">
                              <el-form-item label="模块" class="names">
                                <el-input v-model="condition.like.module"></el-input>
                            </el-form-item>
                            </el-col>
                            <el-col :span="10"  :offset="2">
                                <div class="block">
                                    <span class="demonstration">操作日期</span>
                                    <el-date-picker
                                            v-model="createDate"
                                            type="daterange"
                                            align="right"
                                            value-format="yyyy-MM-dd"
                                            unlink-panels
                                            range-separator="至"
                                            start-placeholder="开始日期"
                                            end-placeholder="结束日期"
                                            :picker-options="pickerOptions">
                                    </el-date-picker>
                                </div>
                            </el-col>
                            <el-col :span="2">
                                <el-button type="primary"  size="small" @click="getOCRFileList">查询</el-button>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
                <div class="table-container">
                    <div class="table-list">
                        <el-table
                                class="tableLimit"
                                :data="tabelData"
                                 height="100%"
                               style="width: 100%;height:100%"
                                :row-class-name="tableRowClassName"
                                :default-sort="{prop: 'createTime', order: 'descending'}">
                            <el-table-column prop="createTime" label="操作时间" width="150" ></el-table-column>
                            <el-table-column prop="username" label="操作员"  width="150"></el-table-column>
                              <el-table-column prop="module" label="模块" width="100" ></el-table-column>
                                <el-table-column  prop="description" label="操作" width="150" ></el-table-column>
                                 <el-table-column prop="ipAddr" label="访问的ip地址"  width="110"></el-table-column>                
                                  <el-table-column   label="详细信息" >
                                        <template slot-scope="scope"  >
                                        <span   class="textover" :title="scope.row.content">{{ scope.row.content }}</span>
                                      </template>
                                  </el-table-column>
                           
                        </el-table>
                    </div>
                </div>
                    <div class="table-pagination">
                        <el-pagination
                                background
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="condition.currentPage"
                                :page-sizes="[10, 15, 20, 30, 50, 100]"
                                :page-size="condition.pageSize"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="total"
                        >
                        </el-pagination>
                    </div>
            </el-main>
        </el-container>
    </div>
</template>

<style>
    .el-table .warning-row {
        background: oldlace;
    }

    .el-table .success-row {
        background: #f0f9eb;
    }
</style>

<script type="text/javascript">

  export default {
    name: 'loginlog',
    data () {
      return {
        createDate:['',''],
        condition:{
          like:{
            username:''
          },
          beginDate: '',
          endDate:'',
          currentPage:1,
          pageSize:10,
        },
        timer: '',
        sceneId: 1,
        isAllChecked: false,
        total: 0,
        tabelData: [],
        selectedFolderIds: [],
        dialogTableVisible: false,
        OCRLoading: false,
        loading: false,
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        value1: '',
        value2: '',
        refreshView:true,
      }
    },
    watch:{
      condition:{
        handler(val){
          this.$store.commit("setEachCondition",{
            name:this.$route.name,
            condition:val
          })
        },
        deep: true
      },
      createDate(val){
          if (this.createDate != null) {
            this.condition.beginDate = this.createDate[0];
            this.condition.endDate = this.createDate[1];
          } else {
            this.condition.beginDate = "";
            this.condition.endDate = "";
          }
      }
    },
    created(){
      var thisCondition=this.$store.state.eachCondition[this.$route.name]
      if(thisCondition){
        this.condition=thisCondition
        if (this.condition.beginDate&&this.condition.endDate) {
          this.createDate=[this.condition.beginDate,this.condition.endDate]
        } else {
          this.createDate=["",""]
        }
        this.refreshView=false
        this.$nextTick(()=>{
          this.refreshView=true
        })
      }
    },
    mounted () {
      this.sceneId = this.$route.params.sceneId || '1'
      this.$nextTick(() => {
        this.getOCRFileList()
      })

      this.$bus.on('refreshList', () => {
        this.getOCRFileList()
      })
      //this.timer = setInterval(this.getOCRFileList, 30000);
    },beforeDestroy(){
      clearInterval(this.timer);
      this.$bus.off("refreshList")
    }, methods: {
       tableRowClassName ({row, rowIndex}) {
        if (rowIndex % 2 == 0) {
          return 'warning-row'
        } else {
          return 'success-row'
        }
        return ''
      },
       getOCRFileList () {
        this.loading = true
        if(this.createDate!=null) {
          this.condition.beginDate = this.createDate[0]
          this.condition.endDate = this.createDate[1]
        }else{
          this.condition.beginDate =''
          this.condition.endDate = ''
        }
        this.$ajax.post('/api/admin/operationlog/queryByCondition', this.condition).then(res => {
          if (res.code == 200) {
            this.tabelData = res.data.content
            this.total = res.data.totalElements
            this.loading = false
          }
        }).catch(err => {
          this.loading = false
          this.$message({
            message: err,
            type: 'error'
          })
        })
      },
      handleSizeChange (pageSize) {
        this.condition.pageSize = pageSize
        this.getOCRFileList()
      },
      handleCurrentChange (page) {
        this.condition.currentPage = page
        this.getOCRFileList()
      }
      
    }
  }

</script>
<style type="text/css" scoped>
     .el-container{
       height: 80vh;
     } 
    .el-main{
      overflow: hidden;
    }
    .el-form{
      height: 50PX;
    }
    .textover {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  cursor: pointer;
}
  
</style>
<style>
    
</style>

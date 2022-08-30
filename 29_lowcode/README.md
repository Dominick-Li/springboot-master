# 1.关键代码描述
## 1.1.后台代码
关键代码类 | 描述
---- | ----
enums包下的ConditionTypeEnum类 | 定义通用列表页面查询类型
enums包下的DataTypeEnum类 | 定义数据库字段类型
model包下的MetaInfo类 | 存储元数据信息
model包下的MetaColumn类 | 存型元数据拥有的字段信息
model.dto包下的类 | 存储通用数据查询修改删除用的类
service.impl包下的MetaServiceImpl类 | 元数据模型业务实现类
service.impl包下的UniversalServiceImpl类 | 元数据模型对应表的通用CRUD业务实现类
util包下的NativeQueryUtil类 | 执行sql的工具类

## 1.2.前端代码
前端使用vue编写,代码在根目录下的vue目录中

组件| 描述
---- | ----
metadata.Index.vue | 模型列表页面
metadata.save.vue | 模型保存页面
universal.Index.vue |通用列表页面
universal.save.vue | 通用数据录入页面

# 2.启动后台项目
 - 修改 applicaition.yml中的数据库连接信息
```$xslt
    url: jdbc:mysql://localhost:3306/lowcode
    username: root
    password: 123456
```
- 1.等后台程序启动后执行2插入数据
- 2.把resources目录下init.sql中数据插入到数据库中


# 3.启动前端项目
```$xslt
cd vue
npm i 
npm run dev
#访问 http://127.0.0.1:8080/  登录系统
```
账号: admin 
密码: 123456

# 4.遗留问题
添加完元数据需要手动刷新浏览器,要不然元数据对应的菜单访问会跳转到登陆页面。


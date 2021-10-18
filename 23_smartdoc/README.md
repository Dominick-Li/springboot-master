# smart-doc
## 官网
官方文档: https://gitee.com/smart-doc-team/smart-doc/wikis/smart-doc
## 模型类自定义@tag使用
https://gitee.com/smart-doc-team/smart-doc/wikis/javadoc%20tag%E4%BD%BF%E7%94%A8/%E8%87%AA%E5%AE%9A%E4%B9%89tag%E4%BD%BF%E7%94%A8

## mvn命令
```
//生成html
mvn -Dfile.encoding=UTF-8 smart-doc:html
//生成markdown
mvn -Dfile.encoding=UTF-8 smart-doc:markdown
// 生成文档推送到Torna平台
mvn -Dfile.encoding=UTF-8 smart-doc:torna-rest
```

# torna
项目部署路径
https://gitee.com/durcframework/torna#%E4%BD%BF%E7%94%A8%E6%AD%A5%E9%AA%A4
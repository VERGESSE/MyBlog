# MyBlog

一个开源的博客系统，Java编写

项目展示 <https://www.vergessen.top/>
项目介绍 [MyBlog博客系统开源](https://www.vergessen.top/article/v/8131054669715631)

安装使用教程
首先下载`blog.sql`文件，放到你的服务器的数据库上执行一下，即可完成表的创建以及一些原始数据的导入
然后下载我提供的`MyBlog.zip`压缩包（60m左右），在服务器上解压安装包，进入`bin`目录，
修改`conf/server.json`，进行图床配置修改

```
{
  "port": ":8600",            // 启动的端口号，不建议修改
  "filePath": "../images",    // 默认图片存放地址，不建议修改 
  "Auth": "ImgGo"             // 你的图床密码，需要修改 
}
```

修改`application.yml配置`，需要修改的地方如下：

```
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  datasource:
    driverClassName: com.mysql.jdbc.Driver
    // 你自己的数据库地址
    url: jdbc:mysql://127.0.0.1:3306/myblog?&serverTimezone=GMT%2B8    
    // 你自己的数据库用户名
    username: root     
    // 你自己的数据库密码
    password: root                                                      
  mail:
    host: smtp.163.com        
    // 你自己的163邮箱地址
    username: vergessentop@163.com 
    // 你自己的163邮箱授权码
    password: yourpassword    
    // 是否开启邮箱服务，如果不会申请163邮箱或者qq邮箱的smtp服务可以置为false关闭邮箱服务                 // 申请方法很简单，搜索引擎随便一搜就行 
    isopen: true
imgGo:
  // 你图床的事迹地址，一般就是你的域名（图床服务经nginx转发的话，ngnix配置下面给出）
  serverUrl: https://www.vergessen.top    
  // 同上面的图床密码
  auth: yourpassword                                                    
  group: blog    
# jwt配置
jwt:
  // 后台登录权限加密掩码，建议必修改，而且定期更新
  # 加密掩码，建议定期修改,长度需要大于256位
  secret: MyBlog--Vergessen--secret--default
  # 有效期，单位秒，默认三天
  expire-time-in-second: 259200
```

 配置完成后执行启动指令

```
./start.sh
```

如果无权执行，执行`chmod 777 start.sh`授权，然后执行上面的指令即可运行本博客
如果想停止本博客，在`bin`目录执行

```
./stop.sh
```

项目需要的nginx配置如下:

```
location ^~/imgGo/ {
  proxy_pass  http://127.0.0.1:8600;
}
location ^~/admin/static/ {
  proxy_pass  http://127.0.0.1:8666;
}
location  ~/ {
  proxy_pass http://127.0.0.1:8666;
  proxy_set_header Host $http_host;
  proxy_set_header X-Real-IP $remote_addr;
  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  proxy_set_header X-Forwarded-Proto $scheme;
}
```

# chatroom-java

本次聊天室所有代码及配置文件都在chatroom目录下

数据库使用mysql，mysql创建脚本路径：chatroom/create.sql (需要自行创建一个数据库：chatroom,然后进入这个数据库，运行该脚本创建表、脚本运行命令> source "create.sql 创建脚本的绝对路径")



本项目使用maven进行构建，在运行时需要注意以下几点：

1.直接在chatroom-java文件夹（即最外层）级别打开工作区

2.加载maven依赖项、jdk为11

2.设置服务端ip地址，端口号默认8080 配置文件路径：chatroom/client/src/main/resources/application.properties

3.在IDEA上打开项目进行构建时，可能会遇到 java: java.lang.IllegalArgumentException 异常，按照IDEA的提示，在设置>构建、执行、

部署>编译器>共享构建过程VM选项中设置  -Djps.track.ap.dependencies=false 即可



项目分为三个模块：服务端、客户端、公共数据模块

一、客户端：

二、服务端：

三、公共数据模块

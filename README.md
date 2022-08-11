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

├── java

│   └── client

│       ├── ChatClient.java   客户端启动类    

│       ├── ClientFriendChatHandler.java    实时接收消息类  

│       ├── FileResponseHandler.java        发送文件时，统计发送进度，并记录在breakPointSend文件中      

│       ├── ReceiveFileHandler.java         接收文件内容，统计接收文件进度，并记录在breakPointReceive文件中  

│       ├── ResponseHandler.java            与客户端进行消息交互 

│       ├── SendFile.java                   启动发送文件事件  
│       ├── StartReceiveFile.java           启动接收文件事件  
│       └── view      所有的界面层
│           ├── EnterView.java
│           ├── FriendApplyView.java
│           ├── FriendView.java
│           ├── GroupEnterView.java
│           ├── GroupView.java
│           ├── MainView.java
│           └── NoticeView.java
└── resources  
    ├── application.properties            配置文件，设置服务端ip地址  
    ├── breakPointReceive                 实现接收时断点续传的进度记录文件  
    └── breakPointSend                    实现发送时断点续传的进度记录文件  

二、服务端：
├── java
│   └── server
│       ├── ChatServer.java         服务端启动类 
│       ├── handler                 所有的业务处理类  
│       └── session     会话层 
│           ├── MailSession.java      发送邮件类，用于注册和找回密码  
│           └── SessionMap.java       会话绑定类，对于上线的每一个用户，将其的ID和其与服务端建立的Channel进行绑定，用于发送消息  
└── resources
    ├── application.properties
    └── c3p0-config.xml

三、公共数据模块
├── message   客户端与服务端进行交互的所有事件的消息类型，所有消息都继承于父类Message 

└── protocol    服务端和客户端之间数据传输一致遵循的编解码协议 

    ├── MessageCodec.java
    └── ProtocolFrameDecoder.java

# NUIT-Android 
广东东软学院安卓开发期末作业  
Java仿微信界面(非kotlin)   
如果帮助你成功应付了期末作业   
请点个免费的star  

### 项目部分代码来源：
项目1: https://github.com/Ulrich2003/End-of-period-summary---Android-schoolwork-entertainment-app-imitating-wechat  
项目2: https://github.com/BooksCup/wechat  
基于项目1，引入部分项目2的Activity和util，再进行修改    
后面需要拿来抄作业的同学，请充分理解代码再使用   

### 2023年6月14日 项目要求清单
 - 四个Activity页面主体内容及跳转 
 - 登录页“取消”和返回键能随时随地退出程序 
   - 实际上使用系统返回键退出程序
 - 登录页记住密码，采用SharedPreferences存储实现记住账号密码功能
   - 实际上使用SharedPreferences直接保存用户信息
 - 消息列表页保存数据至Sqlite数据库 
   - SQLiteDao以及SQLiteHelper
 - 聊天页返回至消息列表页携带回最后一条消息内容，并更新数据库和消息列表页RecyclerView 
   - ConversationAdapter中进入聊天详情时，intent会附带user_id作为request参数，
   - MainActivity的onActivityResult会接收这个参数，并更新对应的list
 - 加载页全屏，无标题栏状态栏。 
 - 布局美观情况 
 - 强制下线
   - activity_main.xml 下面第三个按钮实现该功能
 - 通过JSON获取网络数据
   - WeatherTask获取天气信息，ChatsFragment使用该类


### 表结构如下，sql语句在init.sql。其中包含部份生成的假数据。

1. db_user 用户信息表：
   存储用户的基本信息，uid，name，password，phone，age，pic
2. db_friend 好友关系记录表：
   用于记录用户之前的好友关系。fid，user_uid，friend_ui，time_add
3. db_group 群组记录表：
   用于记录群聊信息：gid，name，creator_uid 创建用户的uid，time_create
4. db_group_members 群组成员记录表：
   用于记录群聊成员信息：gmid，gid，user_uid，invite_uid 邀请者id，invite_type 被邀请的方式（qrcode群二维码，invite直接拉入），user_nickname 当前用户的别名，time_join
5. db_message 消息记录表：
   用于记录用户或者群组之间的通讯记录。mid，sender_uid 发送者的用户ID，is_group 接受人是否是群组对象，receiver_id 接收者ID（可能是db_user，也可能是db_group），is_withdraw （消息是否撤回），time_send

### Activity 逻辑 

Start -> LoadingActivity 验证本地数据，查看是否登录
   - util.DataUtils 获取本地Token（当前版本为uid信息）信息，传递至下一级页面
   - 未登录 -> PhoneNumberLoginActivity (此处的LoginActivity已被抛弃)
     - 完成登录 -> MainActivity
     - 未注册 -> RegisterActivity
   - 已登录 -> MainActivity * 过时的逻辑，等待修改 *
     - ~接收uid -> 通过 SQLiteDao 获取 ChatFriend 关系（子类包含Friend和Group）~
     - 传递对应的 ChatFriend 对象给下一级
     - 选择聊天对象 -> ChatActivity
       - 接收 ChatFriend
       - 获取 id，通过 SQLiteDao 获取聊天记录并加载

### Model 含义
   - ChatFriend 聊天列表的对象，可衍生出子类。拥有一个 ArrayList<Message> 记录当前聊天信息
     - Friend 好友聊天对象，但个人
     - Group 群组聊天对象，群聊，实际逻辑未实现
   - Message 单条信息记录
   - User 用户信息，用于储存当前用户的信息
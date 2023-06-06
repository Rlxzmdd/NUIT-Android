# NUIT-Android
广东东软学院安卓开发期末作业
Java仿微信界面(非kotlin)

项目fork来源：
https://github.com/Ulrich2003/End-of-period-summary---Android-schoolwork-entertainment-app-imitating-wechat

做出大量修改，造福后来人。
表结构如下，sql语句在init.sql。其中包含部份生成的假数据。
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

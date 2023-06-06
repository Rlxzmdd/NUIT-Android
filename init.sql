CREATE DATABASE IF NOT EXISTS db_wechat;
-- 使用pic_forum数据库
USE db_wechat;

CREATE TABLE t_user (
    uid INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    age INT NOT NULL,
    pic VARCHAR(100) NOT NULL
);

CREATE TABLE t_friend (
    fid INT PRIMARY KEY,
    user_uid INT NOT NULL, -- 对应t_user表的uid
    friend_uid INT NOT NULL, -- 对应t_user表的uid
    time_add DATETIME NOT NULL
);

CREATE TABLE t_group (
    gid INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    creator_uid INT NOT NULL, -- 对应t_user表的uid
    time_create DATETIME NOT NULL
);

CREATE TABLE t_group_members (
    gmid INT PRIMARY KEY,
    gid INT NOT NULL, -- 对应t_group表的gid
    user_uid INT NOT NULL, -- 对应t_user表的uid
    invite_uid INT NOT NULL, -- 对应t_user表的uid
    invite_type ENUM('qrcode', 'invite') NOT NULL,
    user_nickname VARCHAR(20) NOT NULL,
    time_join DATETIME NOT NULL
);

CREATE TABLE t_message (
    mid INT PRIMARY KEY,
    sender_uid INT NOT NULL, -- 对应t_user表的uid
    is_group BOOLEAN NOT NULL,
    receiver_id INT NOT NULL, -- 对应t_user或t_group表的uid或gid，根据is_group判断
    is_withdraw BOOLEAN NOT NULL DEFAULT FALSE,
    time_send DATETIME NOT NULL
);

-- 插入用户数据
INSERT INTO db_user (uid, name, password, phone, age, pic) VALUES (1, 'admin', '1234', '12345678901', 21, 'R.drawable.cai');
INSERT INTO db_user (uid, name, password, phone, age, pic) VALUES (2, 'Alice', '1234', '12345678902', 20, 'R.drawable.alice');
INSERT INTO db_user (uid, name, password, phone, age, pic) VALUES (3, 'Bob','1234','12345678903',24,'R.drawable.Bob');
INSERT INTO db_user (uid, name, password, phone, age, pic) VALUES (4, 'Charlie', '1234', '12345678904', 21, 'R.drawable.charlie');
INSERT INTO db_user (uid, name, password, phone, age, pic) VALUES (5, 'David', '1234', '12345678905', 23, 'R.drawable.david');

-- 插入好友数据
INSERT INTO db_friend (fid, user_uid, friend_uid, time_add) VALUES (1, 1, 2, '2021-12-20 10:00:00'); -- admin和Alice是好友
INSERT INTO db_friend (fid, user_uid, friend_uid, time_add) VALUES (2, 1, 3, '2021-12-20 10:10:00'); -- admin和Bob是好友
INSERT INTO db_friend (fid, user_uid ,friend_uid ,time_add) VALUES (3 ,2 ,3 ,'2021-12-20 10:20:00'); -- Alice和Bob是好友
INSERT INTO db_friend (fid ,user_uid ,friend_uid ,time_add) VALUES (4 ,2 ,4 ,'2021-12-20 10:30:00'); -- Alice和Charlie是好友
INSERT INTO db_friend (fid ,user_uid ,friend_uid ,time_add) VALUES (5 ,3 ,5 ,'2021-12-20 10:40:00'); -- Bob和David是好友

-- 插入群组数据
INSERT INTO db_group (gid ,name ,creator_uid ,time_create) VALUES (1 ,'2023讨论群' ,1 ,'2021-12-20 11:00:00'); -- admin创建了一个群组
INSERT INTO db_group (gid ,name ,creator_uid ,time_create) VALUES (2 ,'503' ,2 ,'2021-12-20 11:10:00'); -- Alice创建了一个群组

-- 插入群组成员数据
INSERT INTO db_group_members (gmid ,gid ,user_uid ,invite_uid ,invite_type ,user_nickname ,time_join) VALUES (1 ,1 ,1 ,0 ,'qrcode' ,'admin' ,'2021-12-20 11:00:00'); -- admin通过二维码加入了自己创建的群组
INSERT INTO db_group_members (gmid ,gid ,user_uid ,invite_uid ,invite_type ,user_nickname ,time_join) VALUES (2 ,1 ,2 ,1 ,'invite' ,'Alice' ,'2021-12-20 11:05:00'); -- admin邀请Alice加入了群组
INSERT INTO db_group_members (gmid ,gid ,user_uid ,invite_uid ,invite_type ,user_nickname ,time_join) VALUES (3 ,1 ,3 ,2 ,'invite' ,'Bob' ,'2021-12-20 11:06:00'); -- Alice邀请Bob加入了群组
INSERT INTO db_group_members (gmid,gid,user_uid,invite_uid,invite_type,user_nickname,time_join) VALUES (4,2,2,0,'qrcode','Alice','2021-12-20 11:10:00'); -- Alice通过二维码加入了自己创建的群组
INSERT INTO db_group_members (gmid,gid,user_uid,invite_uid,invite_type,user_nickname,time_join) VALUES (5,2,4,2,'invite','Charlie','2021-12-20 11:15:00'); -- Alice邀请Charlie加入了群组

-- 插入消息数据
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (1,'Hi Alice',1,FALSE,2,FALSE,'2021-12-20 11:30:00'); -- admin给Alice发送消息
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (2,'Hi admin',2,FALSE,1,FALSE,'2021-12-20 11:31:00'); -- Alice给admin回复消息
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (3,'Hello Bob',1,FALSE,3,FALSE,'2021-12-20 11:32:00'); -- admin给Bob发送消息
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (4,'Hello admin',3,FALSE,1,FALSE,'2021-12-20 11:33:00'); -- Bob给admin回复消息
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (5,'Hey Alice',3,FALSE,2,FALSE,'2021-12-20 11:34:00'); -- Bob给Alice发送消息
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (6,'Hey Bob',2,FALSE,3,FALSE,'2021-12-20 11:35:00'); -- Alice给Bob回复消息
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (7,'Hello everyone',1,TRUE,1,FALSE,'2021-12-20 11:36:00'); -- admin在群组中发送消息
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (8,'Hello admin',2,TRUE,1,FALSE,'2021-12-20 11:37:00'); -- Alice在群组中回复消息
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (9,'Hi guys',4,FALSE,2,FALSE,'2021-12-20 11:38:00'); -- Charlie给Alice发送消息
INSERT INTO db_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (10,'Hi Charlie',2,FALSE,4,FALSE,'2021-12-20 11:39:00'); -- Alice给Charlie回复消息

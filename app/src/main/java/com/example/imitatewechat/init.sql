CREATE TABLE user (
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    age INT NOT NULL,
    pic VARCHAR(100) NOT NULL
);

CREATE TABLE friend (
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (friend_id) REFERENCES user(id)
);

CREATE TABLE message (
    id INT PRIMARY KEY,
    content VARCHAR(200) NOT NULL,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    time DATETIME NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES user(id),
    FOREIGN KEY (receiver_id) REFERENCES user(id)
);

-- 插入用户数据
INSERT INTO user (id, name, password, age, pic) VALUES (2, 'Alice', '1234', 20, 'R.drawable.alice');
INSERT INTO user (id, name, password, age, pic) VALUES (3, 'Bob', '1234', 22, 'R.drawable.bob');
INSERT INTO user (id, name, password, age, pic) VALUES (4, 'Charlie', '1234', 21, 'R.drawable.charlie');
INSERT INTO user (id, name, password, age, pic) VALUES (5, 'David', '1234', 23, 'R.drawable.david');

-- 插入好友数据
INSERT INTO friend (user_id, friend_id) VALUES (1, 2); -- admin和Alice是好友
INSERT INTO friend (user_id, friend_id) VALUES (1, 3); -- admin和Bob是好友
INSERT INTO friend (user_id, friend_id) VALUES (2, 3); -- Alice和Bob是好友
INSERT INTO friend (user_id, friend_id) VALUES (2, 4); -- Alice和Charlie是好友
INSERT INTO friend (user_id, friend_id) VALUES (3, 5); -- Bob和David是好友

-- 插入消息数据
INSERT INTO message (id, content, sender_id, receiver_id) VALUES (1, 'Hi Alice', 1, 2); -- admin给Alice发送消息
INSERT INTO message (id, content, sender_id, receiver_id) VALUES (2, 'Hi admin', 2, 1); -- Alice给admin回复消息
INSERT INTO message (id, content, sender_id, receiver_id) VALUES (3, 'Hello Bob', 1 ,3); -- admin给Bob发送消息
INSERT INTO message (id, content, sender_id ,receiver_id) VALUES (4 , 'Hello admin', 3 ,1); -- Bob给admin回复消息
INSERT INTO message (id ,content ,sender_id ,receiver_id) VALUES (5 , 'Hey Alice' ,3 ,2); -- Bob给Alice发送消息
INSERT INTO message (id ,content ,sender_id ,receiver_id) VALUES (6 , 'Hey Bob' ,2 ,3); -- Alice给Bob回复消息
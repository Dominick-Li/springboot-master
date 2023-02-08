drop database if exists  boot_master;
create database boot_master;
use boot_master;
drop table if exists  sys_users;

CREATE TABLE sys_users (
id int  AUTO_INCREMENT,
username varchar(20) COMMENT '用户名',
password varchar(20) COMMENT '密码',
create_time datetime COMMENT '创建时间',
update_time datetime COMMENT '修改时间',
grade   int COMMENT '年纪',
 PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into sys_users(username, password, create_time,update_time,grade) values ('test1','123456','2020-7-1 0:02','2020-7-1 0:02',7),('test2','123456','2020-7-1 0:02','2020-7-1 0:02',7),
('test3','123456','2020-7-1 0:02','2020-7-1 0:02',7),('test4','123456','2020-7-1 0:02','2020-7-1 0:02',8),('test5','123456','2020-7-1 0:02','2020-7-1 0:02',8),
('test6','123456','2020-7-1 0:02','2020-7-1 0:02',8),('test7','123456','2020-7-1 0:02','2020-7-1 0:02',9),('test8','123456','2020-7-1 0:02','2020-7-1 0:02',9),
('test9','123456','2020-7-1 0:02','2020-7-1 0:02',9),('test10','123456','2020-7-1 0:02','2020-7-1 0:02',9),('test11','123456','2020-7-1 0:02','2020-7-1 0:02',9),
('test12','123456','2020-7-1 0:02','2020-7-1 0:02',9);
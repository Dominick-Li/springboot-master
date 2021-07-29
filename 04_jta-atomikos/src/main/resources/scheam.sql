create database orders;
use orders;
create table orders(
id varchar(40),
name varchar(25)
);
create database orderdetails;
use orderdetails;
create table orderdetails(
id int,
ordcerId  varchar(40)
);
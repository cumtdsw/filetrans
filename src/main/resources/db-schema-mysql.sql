DROP TABLE IF EXISTS `task_info`;
DROP TABLE IF EXISTS `exception_info`;
DROP TABLE IF EXISTS `dictionary`;
DROP TABLE IF EXISTS `dictionary_data`;


create table task_info(id varchar(255) PRIMARY KEY,toIP varchar(255),toPath varchar(255),dataType int,status int,createTime datetime,startTime datetime,endTime datetime,dataPath varchar(255),backup1 varchar(255),backup2 varchar(255),backup3 varchar(255));
create table exception_info(id varchar(255) PRIMARY KEY,createTime datetime,content varchar(5000),backup1 varchar(255),backup2 varchar(255),backup3 varchar(255));
create table dictionary(id int PRIMARY KEY auto_increment,value int,name varchar(255));
create table dictionary_data(id int PRIMARY KEY auto_increment,dictValue int,dataValue int,dataName varchar(255));
insert into dictionary(value,name) values(1,'taskStatus');
insert into dictionary(value,name) values(2,'exceptionStatus');
insert into dictionary(value,name) values(3,'queueStatus');
insert into dictionary(value,name) values(4,'dataType');
insert into dictionary_data(dictValue,dataName,dataValue) values(1,'waiting',0);
insert into dictionary_data(dictValue,dataName,dataValue) values(1,'executing',1);
insert into dictionary_data(dictValue,dataName,dataValue) values(1,'completed',2);
insert into dictionary_data(dictValue,dataName,dataValue) values(1,'failed',3);
insert into dictionary_data(dictValue,dataName,dataValue) values(2,'waiting',0);
insert into dictionary_data(dictValue,dataName,dataValue) values(2,'executing',1);
insert into dictionary_data(dictValue,dataName,dataValue) values(2,'completed',2);
insert into dictionary_data(dictValue,dataName,dataValue) values(3,'waiting',0);
insert into dictionary_data(dictValue,dataName,dataValue) values(3,'started',1);
insert into dictionary_data(dictValue,dataName,dataValue) values(4,'file',0);
insert into dictionary_data(dictValue,dataName,dataValue) values(4,'fileGroup',1);
insert into dictionary_data(dictValue,dataName,dataValue) values(4,'folder',2);
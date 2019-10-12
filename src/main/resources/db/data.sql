update dc_task set status=0;

insert into dc_devcie(id,name,alias,ip_address,user_name,port,os_type,status) values (1,'node-70','dev-70','192.168.0.70','root',22,'centos7.x', 4);
insert into dc_devcie(id,name,alias,ip_address,user_name,port,os_type,status) values (2,'node-71','dev-71','192.168.0.71','root',22,'centos7.x', 4);
insert into dc_devcie(id,name,alias,ip_address,user_name,port,os_type,status) values (3,'node-72','dev-72','192.168.0.72','root',22,'centos7.x', 4);
insert into dc_devcie(id,name,alias,ip_address,user_name,port,os_type,status) values (4,'node-73','dev-73','192.168.0.73','root',22,'centos7.x', 4);

insert into dc_group (id,name,desc) values (1,'redis-group','redis desc help');
insert into dc_group (id,name,desc) values (2, 'java-ms','java microservice help');

insert into DC_GROUP_DEVICE_REF (group_id,device_id) values (1,1);
insert into DC_GROUP_DEVICE_REF (group_id,device_id) values (1,2);
insert into DC_GROUP_DEVICE_REF (group_id,device_id) values (2,3);
insert into DC_GROUP_DEVICE_REF (group_id,device_id) values (2,4);

insert into dc_app_module (id,name,path,pack_dir,pack_ver,pack_file,help,desc,allow_delete) values (1, 'Java-SDK','java_sdk.sh', 'packages','jdk1.8.0_221', 'jdk-8u221-linux-x64.tar.gz','help','desc',0);
insert into dc_app_module (id,name,path,pack_dir,pack_ver,pack_file,help,desc,allow_delete) values (2, '注册中心','java_ms_registe_center.sh', 'packages/java_ms','1.0', 'dfas-register-5.1.3.33.jar','help','desc',0);

insert into dc_strategy (id,name,path,help,desc,allow_delete) values (1,'java-ms-registe-center', 'java_ms_registe_center_s.sh','java-ms help','description',1);

insert into dc_task (id,strategy_id,group_id,status) values (10,1,1,0);
insert into dc_task (id,strategy_id,group_id,status) values (11,1,2,0);


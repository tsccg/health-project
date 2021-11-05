/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/4/8 14:20:21                            */
/*==============================================================*/


drop table if exists t_order;

drop table if exists t_user;

/*==============================================================*/
/* Table: t_order                                               */
/*==============================================================*/
create table t_order
(
   id                   int,
   ordername            varbinary(30),
   user_id              int
);

/*==============================================================*/
/* Table: t_user                                                */
/*==============================================================*/
create table t_user
(
   id                   int not null auto_increment,
   name                 varchar(30),
   primary key (id)
);

alter table t_order add constraint FK_Reference_1 foreign key (user_id)
      references t_user (id) on delete restrict on update restrict;


create table post(
    post_id         bigint auto_increment 			   primary key,
    image_id        varchar(255)                       not null,
    uploader_id     bigint                             not null,
    upload_datetime datetime default CURRENT_TIMESTAMP null,
    contents        text                               null
);

create table user(
    user_id  bigint auto_increment primary key,
    username varchar(255)          not null,
    email    varchar(255)          not null,
    password varchar(255)          not null
);

create table follow(
    follow_id          bigint auto_increment              primary key,
    user_id            bigint                             not null,
    follower_id        bigint                             not null,
    follow_datetime    datetime default CURRENT_TIMESTAMP null,
    mail_sent_datetime datetime default null
);
create schema plog_ums;
create schema plog_blog;


create sequence plog_ums.user_id_seq as integer;

create table if not exists plog_ums.user
(
    id                serial
        constraint user_pk primary key,
    email             varchar(50)             not null,
    password          varchar(512)            not null,
    first_name        varchar(20)             not null,
    last_name         varchar(20),
    nickname          varchar(20),
    join_dt           timestamp default now() not null,
    birth             date,
    sex               varchar(8),
    profile_image_url varchar
);
create unique index user_email_uindex on plog_ums.user(email);

alter sequence plog_ums.user_id_seq owned by plog_ums.user.id;

create sequence plog_ums.user_roles_id_seq;


create table if not exists plog_ums.user_role
(
    id      integer     default nextval('plog_ums.user_roles_id_seq') not null
        constraint user_roles_pk primary key,
    user_id integer
        constraint user_role_user_id_fk references plog_ums.user
            on update cascade on delete cascade,
    role    varchar(20) default 'ROLE_USER'
);

alter sequence plog_ums.user_roles_id_seq owned by plog_ums.user_role.id;


create sequence plog_blog.blog_info_id_seq;

create table if not exists plog_blog.blog
(
    id          integer   default nextval('plog_blog.blog_info_id_seq') not null
        constraint blog_info_pk primary key,
    blog_name   varchar(50) unique,
    user_id     integer
        constraint blog_user_id_fk references plog_ums.user
            on update cascade on delete cascade,
    short_intro varchar(100),
    intro_html  text,
    update_dt   timestamp default now(),
    create_dt   timestamp default now(),
    intro_md    text
);

alter sequence plog_blog.blog_info_id_seq owned by plog_blog.blog.id;


create sequence plog_blog.category_id_seq as integer;

create table if not exists plog_blog.category
(
    id            serial
        constraint category_pk primary key,
    category_name varchar(100) not null,
    category_desc varchar(1000),
    blog_id       integer
        constraint category_blog_id_fk references plog_blog.blog
            on update cascade on delete cascade
);

alter sequence plog_blog.category_id_seq owned by plog_blog.category.id;

create unique index if not exists category_blog_id_category_name_uindex
    on plog_blog.category (blog_id, category_name);


create sequence plog_blog.link_id_seq as integer;

create table if not exists plog_blog.link
(
    id        serial
        constraint link_pk primary key,
    blog_id   integer
        constraint link_blog_id_fk references plog_blog.blog
            on update cascade on delete cascade,
    link_name varchar(20),
    url       text
);

alter sequence plog_blog.link_id_seq owned by plog_blog.link.id;

create sequence plog_blog.visible_type_id_seq;

create table if not exists plog_blog.state
(
    id         integer default nextval('plog_blog.visible_type_id_seq') not null
        constraint visible_type_pk primary key,
    state_name varchar(10)
);

alter sequence plog_blog.visible_type_id_seq owned by plog_blog.state.id;


create sequence plog_blog.posting_id_seq as integer;

create table if not exists plog_blog.posting
(
    id                  serial
        constraint posting_pk primary key,
    title               varchar(200)                               not null,
    html_content        text                                       not null,
    user_id             integer
        constraint posting_user_id_fk references plog_ums.user
            on update cascade on delete cascade,
    category_id         integer
        constraint posting_category_id_fk references plog_blog.category
            on update cascade on delete cascade,
    blog_id             integer
        constraint posting_blog_id_fk references plog_blog.blog
            on update cascade on delete cascade,
    state_id            integer
        constraint posting_state_id_fk references plog_blog.state
            on update cascade on delete cascade,
    hit_cnt             integer      default 0,
    create_dt           timestamp    default now()                 not null,
    update_dt           timestamp    default now()                 not null,
    is_comment_allowed  boolean      default true                  not null,
    is_star_allowed     boolean      default true                  not null,
    thumbnail_image_url text,
    md_content          text                                       not null,
    constraint posting_check
        check (update_dt >= create_dt)
);



alter sequence plog_blog.posting_id_seq owned by plog_blog.posting.id;

create sequence plog_blog.posting_star_id_seq as integer;


create sequence plog_blog.comment_id_seq as integer;

create table if not exists plog_blog.comment
(
    id                serial primary key unique,
    user_id           integer
        constraint comment_user_id_fk references plog_ums.user
            on update cascade on delete cascade,
    posting_id        integer
        constraint comment_posting_id_fk references plog_blog.posting
            on update cascade on delete cascade,
    parent_comment_id integer
        constraint comment_comment_id_fk references plog_blog.comment
            on update cascade on delete cascade,
    depth             integer   default 1
        constraint comment_depth_check check (depth <= 2),
    comment_content   varchar(500)            not null,
    is_secret         boolean   default false not null,
    create_dt         timestamp default now() not null,
    update_dt         timestamp default now() not null
);


alter sequence plog_blog.comment_id_seq owned by plog_blog.comment.id;


create table if not exists plog_blog.posting_star
(
    id         serial
        constraint posting_star_pk primary key,
    user_id    integer
        constraint posting_star_user_id_fk references plog_ums.user
            on update cascade on delete cascade,
    posting_id integer
        constraint posting_star_posting_id_fk references plog_blog.posting
            on update cascade on delete cascade
);

create sequence plog_blog.tag_id_seq as integer;

create table if not exists plog_blog.tag
(
    id       serial primary key,
    blog_id  integer not null
        constraint tag_blog_id_fk references plog_blog.blog
            on update cascade on delete cascade,
    tag_name varchar(50)
);

alter sequence plog_blog.tag_id_seq owned by plog_blog.tag.id;

create unique index tag_blog_id_tag_name_uindex
    on plog_blog.tag (blog_id, tag_name);

alter sequence plog_blog.posting_star_id_seq owned by plog_blog.posting_star.id;

create sequence plog_blog.posting_tag_id_seq as integer;

create table if not exists plog_blog.posting_tag
(
    id         serial
        constraint posting_tag_pk primary key,
    tag_id     integer not null
        constraint posting_tag_tag_id_fk references plog_blog.tag
            on update cascade on delete cascade,
    posting_id integer not null
        constraint posting_tag_posting_id_fk references plog_blog.posting
            on update cascade on delete cascade
);

alter sequence plog_blog.posting_tag_id_seq owned by plog_blog.posting_tag.id;

create sequence plog_blog.subscribe_id_seq as integer;

create table if not exists plog_blog.subscribe
(
    id        serial
        constraint subscribe_pk primary key,
    user_id   integer                 not null
        constraint subscribe_user_id_fk references plog_ums.user
            on update cascade on delete cascade,
    blog_id   integer                 not null
        constraint subscribe_blog_id_fk references plog_blog.blog
            on update cascade on delete cascade,
    create_dt timestamp default now() not null
);

alter sequence plog_blog.subscribe_id_seq owned by plog_blog.subscribe.id;

create view plog_blog.v_posting
            (id, blog_id, category_id, thumbnail_image_url, title, html_content, md_content, is_star_allowed,
             is_comment_allowed, state_id, hit_cnt, create_dt, update_dt, user_id, posting_star_count)
as
WITH posting_star_count AS (SELECT ps.posting_id,
                                   count(*) AS posting_star_count
                            FROM plog_blog.posting_star ps
                            GROUP BY ps.posting_id)
SELECT p.id,
       p.blog_id,
       p.category_id,
       p.thumbnail_image_url,
       p.title,
       p.html_content,
       p.md_content,
       p.is_star_allowed,
       p.is_comment_allowed,
       p.state_id,
       p.hit_cnt,
       p.create_dt,
       p.update_dt,
       p.user_id,
       COALESCE(psc.posting_star_count, 0::bigint) AS posting_star_count
FROM plog_blog.posting p
         LEFT JOIN posting_star_count psc ON p.id = psc.posting_id;


create view plog_blog.v_hot_posting
            (id, blog_id, category_id, thumbnail_image_url, title, html_content, md_content, is_star_allowed,
             is_comment_allowed, state_id, hit_cnt, create_dt, update_dt, user_id, posting_star_count, star_row_num)
as
SELECT t.id,
       t.blog_id,
       t.category_id,
       t.thumbnail_image_url,
       t.title,
       t.html_content,
       t.md_content,
       t.is_star_allowed,
       t.is_comment_allowed,
       t.state_id,
       t.hit_cnt,
       t.create_dt,
       t.update_dt,
       t.user_id,
       t.posting_star_count,
       t.star_row_num
FROM (SELECT p.id,
             p.blog_id,
             p.category_id,
             p.thumbnail_image_url,
             p.title,
             p.html_content,
             p.md_content,
             p.is_star_allowed,
             p.is_comment_allowed,
             p.state_id,
             p.hit_cnt,
             p.create_dt,
             p.update_dt,
             p.user_id,
             p.posting_star_count,
             row_number() OVER (PARTITION BY p.blog_id ORDER BY p.posting_star_count DESC) AS star_row_num
      FROM plog_blog.v_posting p) t
WHERE t.star_row_num <= 5;



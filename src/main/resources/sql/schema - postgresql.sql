create database db1;

create table if not exists roles
(
  id bigint not null constraint roles_pkey
  primary key,
  name varchar(255)
)
;

create table if not exists users
(
  id bigserial not null
    constraint users_pkey
    primary key,
  blocked boolean not null,
  confirmed boolean not null,
  email varchar(255),
  password varchar(255),
  timestamp timestamp,
  username varchar(255)
)
;

create table if not exists user_role
(
  user_id bigint not null
    constraint fkj345gk1bovqvfame88rcx7yyx
    references users,
  role_id bigint not null
    constraint fkt7e7djp752sqn6w22i6ocqy6q
    references roles,
  constraint user_role_pkey
  primary key (user_id, role_id)
)
;

create table if not exists user_attributes
(
  id bigserial not null
    constraint user_attributes_pkey
    primary key,
  name varchar(255),
  enabled boolean not null
)
;

create table if not exists user_params
(
  value varchar(255),
  user_id bigint not null
    constraint fkd5gwh5sm7n2ct1m4naikqiuds
    references users,
  attribute_id bigint not null
    constraint fk6yhm7v5un3ic08d6p3wc4v3v6
    references user_attributes,
  constraint user_params_pkey
  primary key (attribute_id, user_id)
)
;

create table if not exists categories
(
  id bigserial not null
    constraint categories_pkey
    primary key,
  name varchar(255),
  parent_id bigint
    constraint fksaok720gsu4u2wrgbk10b5n8d
    references categories
)
;

create table if not exists images
(
  id bigserial not null
    constraint images_pkey
    primary key,
  data text
)
;

create table if not exists articles
(
  id bigserial not null
    constraint articles_pkey
    primary key,
  content text,
  timestamp timestamp,
  title varchar(255),
  author_id bigint
    constraint fke02fs2ut6qqoabfhj325wcjul
    references users,
  category_id bigint
    constraint fk7i4rryg7kqwyyrr08temnc71e
    references categories,
  image_id bigint
    constraint fka34itdax1hb0tyupg9sy97hnq
    references images,
  popularity integer default 0 not null
)
;

create table if not exists comments
(
  id bigserial not null
    constraint comments_pkey
    primary key,
  text varchar(255),
  timestamp timestamp,
  article_id bigint
    constraint fkk4ib6syde10dalk7r7xdl0m5p
    references articles,
  author_id bigint
    constraint fkn2na60ukhs76ibtpt9burkm27
    references users
)
;

create table if not exists ratings
(
  value varchar(255),
  article_id bigint not null
    constraint fk3eoj5nyamcfsnuljyejmr4lom
    references articles,
  user_id bigint not null
    constraint fkb3354ee2xxvdrbyq9f42jdayd
    references users,
  timestamp timestamp,
  constraint ratings_pkey
  primary key (article_id, user_id)
)
;

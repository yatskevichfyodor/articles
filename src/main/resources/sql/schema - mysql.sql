create database db1;
use db1;

create table if not exists roles
(
  id bigint not null,
  name varchar(255),
  PRIMARY KEY (id)
)
;

create table if not exists users
(
  id BIGINT not null,
  blocked boolean not null,
  confirmed boolean not null,
  email varchar(255),
  password varchar(255),
  timestamp timestamp,
  username varchar(255),
  PRIMARY KEY (id)
)
;

create table if not exists user_role
(
  user_id bigint not null,
  role_id bigint not null,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id)
)
;

create table if not exists user_attributes
(
  id BIGINT not null,
  name varchar(255),
  enabled boolean not null,
  PRIMARY KEY (id)
)
;

create table if not exists user_params
(
  value varchar(255),
  user_id bigint not null,
  attribute_id bigint not null,
  PRIMARY KEY (attribute_id, user_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (attribute_id) REFERENCES user_attributes(id)
)
;

create table if not exists categories
(
  id BIGINT not null,
  name varchar(255),
  parent_id bigint,
  PRIMARY KEY (id),
  FOREIGN KEY (parent_id) REFERENCES categories(id)
)
;

create table if not exists images
(
  id BIGINT not null,
  data text,
  PRIMARY KEY (id)
)
;

create table if not exists articles
(
  id BIGINT not null,
  content text,
  timestamp timestamp,
  title varchar(255),
  author_id bigint,
  category_id bigint,
  image_id bigint,
  popularity integer default 0 not null,
  PRIMARY KEY (id),
  FOREIGN KEY (author_id) REFERENCES users(id),
  FOREIGN KEY (category_id) REFERENCES categories(id),
  FOREIGN KEY (image_id) REFERENCES images(id)
)
;

create table if not exists comments
(
  id BIGINT not null,
  text varchar(255),
  timestamp timestamp,
  article_id bigint,
  author_id bigint,
  PRIMARY KEY (id),
  FOREIGN KEY (article_id) REFERENCES articles(id),
  FOREIGN KEY (author_id) REFERENCES users(id)
)
;

create table if not exists ratings
(
  value varchar(255),
  article_id bigint not null,
  user_id bigint not null,
  timestamp timestamp,
  constraint ratings_pkey
  PRIMARY KEY (article_id, user_id),
  FOREIGN KEY (article_id) REFERENCES articles(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
)
;

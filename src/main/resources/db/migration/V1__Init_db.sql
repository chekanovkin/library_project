create sequence hibernate_sequence start 1 increment 1;

create table author (
  id int8 not null,
  name varchar(255),
  patronymic varchar(255),
  surname varchar(255),
  primary key (id)
);

create table book (
  id int8 not null,
  amount int4 not null,
  description varchar(255),
  name varchar(255),
  year int4 not null,
  author_id int8,
  primary key (id)
);

create table book_genre (
  genre_id int8 not null,
  book_id int8 not null,
  primary key (book_id, genre_id)
);

create table genre (
  id int8 not null,
  name varchar(255),
  primary key (id)
);

create table user_role (
  user_id int8 not null,
  roles varchar(255)
);

create table usr (
id int8 not null,
  email varchar(255),
  login varchar(255),
  name varchar(255),
  password varchar(255),
  patronymic varchar(255),
  surname varchar(255),
  primary key (id)
);

alter table if exists usr
add constraint UK_b2j2bjirhqhbg1rsexaq5qs9x unique (login);

alter table if exists book
add constraint FKklnrv3weler2ftkweewlky958
foreign key (author_id) references author;

alter table if exists book_genre
add constraint FK52evq6pdc5ypanf41bij5u218
foreign key (book_id) references book;

alter table if exists book_genre
add constraint FK8l6ops8exmjrlr89hmfow4mmo
foreign key (genre_id) references genre;

alter table if exists user_role
add constraint FKfpm8swft53ulq2hl11yplpr5
foreign key (user_id) references usr;
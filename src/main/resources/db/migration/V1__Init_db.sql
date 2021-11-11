create sequence hibernate_sequence start 1 increment 1;

create table author (
    id int8 not null, 
    name varchar(50),
    patronymic varchar(50),
    surname varchar(50),
    primary key (id)
);

create table book (
    id int8 not null, 
    amount int4 not null, 
    description varchar(400),
    name varchar(100),
    year int4 not null,
    filename varchar(100),
    primary key (id)
);

create table book_genre (
    genre_id int8 not null, 
    book_id int8 not null, 
    primary key (book_id, genre_id)
);

create table book_author (
    author_id int8 not null,
    book_id int8 not null,
    primary key (book_id, author_id)
);

create table genre (
    id int8 not null, 
    name varchar(50),
    primary key (id)
);

create table library_card (
    id int8 not null, 
    delivery_date date,
    receiving_date date,
    book_id int8, 
    user_id int8, 
    primary key (id)
);

create table user_role (
    user_id int8 not null, 
    roles varchar(15)
);

create table usr (
    id int8 not null,
    email varchar(70),
    login varchar(40),
    name varchar(50),
    password varchar(255),
    patronymic varchar(50),
    surname varchar(50),
    reserved_books int4 not null,
    primary key (id)
);

alter table if exists usr 
add constraint UK_b2j2bjirhqhbg1rsexaq5qs9x unique (login);

alter table if exists book_author
add constraint FK52evq6pdc5ypanf41bij5u219 foreign key (book_id) references book;

alter table if exists book_author
add constraint FK52evq6pdc5ypanf41bij5u220 foreign key (author_id) references book;

alter table if exists book_genre 
add constraint FK52evq6pdc5ypanf41bij5u218 foreign key (book_id) references book;

alter table if exists book_genre 
add constraint FK8l6ops8exmjrlr89hmfow4mmo foreign key (genre_id) references genre;

alter table if exists library_card 
add constraint FK5sa3r45pychnbva5fqd8cw13m foreign key (book_id) references book;

alter table if exists library_card 
add constraint FKjc55ctr726uq5fdwv5ff72yig foreign key (user_id) references usr;

alter table if exists user_role 
add constraint FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr;
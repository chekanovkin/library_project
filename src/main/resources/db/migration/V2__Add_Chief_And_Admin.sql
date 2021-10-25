insert into usr (id, login, password, name, surname, patronymic, email, reserved_books)
values (1, 'admin', 'admin', 'Андрей', 'Чекановкин', 'Евгеньевич', 'ymnyaga@yandex.ru', 0),
(2, 'user', 'user', 'Никита', 'Шачнов', 'Алексеевич', 'shachnov@yandex.ru', 0);

insert into user_role (user_id, roles)
values(1, 'ADMIN'), (1, 'LIBRARIAN'), (2, 'USER');
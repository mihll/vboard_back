insert into db_vboard.users_shared (user_id, created_by, created_date, last_modified_by, last_modified_date, email,
                                    enabled, password, profile_pic_filename)
values (1, '127.0.0.1', '2021-05-14 17:28:40.304000', '127.0.0.1', '2021-05-14 17:29:02.179000',
        'jan.kowalski@example.com', true, '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO',
        'defaultPersonProfilePic.jpg'),
       (2, '127.0.0.1', '2021-05-14 17:28:40.304000', '2', '2021-05-14 19:48:49.369000', 'lidia.lukasik@example.com', true,
        '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO', 'defaultPersonProfilePic.jpg'),
       (3, '127.0.0.1', '2021-05-14 17:28:40.304000', '3', '2021-05-14 19:53:56.795000', 'michalina.madej@example.com', true,
        '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO', 'defaultPersonProfilePic.jpg'),
       (4, '127.0.0.1', '2021-05-14 17:28:40.304000', '4', '2021-05-14 19:56:53.204000', 'mikolaj.czyz@example.com',
        true, '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO',
        'defaultPersonProfilePic.jpg'),
       (5, '127.0.0.1', '2021-05-14 17:28:40.304000', '127.0.0.1', '2021-05-14 17:29:02.179000', 'michal.musial@example.com',
        true, '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO',
        'defaultPersonProfilePic.jpg'),
       (6, '127.0.0.1', '2021-05-14 17:28:40.304000', '127.0.0.1', '2021-05-14 17:29:02.179000',
        'adam.zak@example.com', true, '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO',
        'defaultPersonProfilePic.jpg'),
       (7, '127.0.0.1', '2021-05-14 17:28:40.304000', '127.0.0.1', '2021-05-14 17:29:02.179000',
        'szkola_muzyczna@example.com', true, '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO',
        'defaultInstitutionProfilePic.jpg'),
       (8, '127.0.0.1', '2021-05-14 17:28:40.304000', '8', '2021-05-17 23:06:38.941000', 'jaworzno@example.com', true,
        '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO', 'defaultInstitutionProfilePic.jpg'),
       (9, '127.0.0.1', '2021-05-14 17:28:40.304000', '127.0.0.1', '2021-05-14 17:29:02.179000',
        'majowe_osiedle@example.com', true, '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO',
        'defaultInstitutionProfilePic.jpg'),
       (10, '127.0.0.1', '2021-05-14 17:28:40.304000', '10', '2021-05-14 19:43:51.147000', 'vboard@example.com', true,
        '$2a$10$ZLnAsrnnc50PPvoBOqY4BePXy6kM8PdOjINidDQSufhZVrWo0quwO', 'defaultInstitutionProfilePic.jpg');
commit;

insert into db_vboard.person_users (birth_date, first_name, last_name, user_id)
values (null, 'Jan', 'Kowalski', 1),
       (null, 'Lidia', 'Łukasik', 2),
       (null, 'Michalina', 'Madej', 3),
       (null, 'Mikołaj', 'Czyż', 4),
       (null, 'Michał', 'Musiał', 5),
       (null, 'Adam', 'Żak', 6);
commit;

insert into db_vboard.institution_users (address_city, address_post_code, address_street, institution_name, user_id)
values (null, null, null, 'Państwowa Szkoła Muzyczna I stopnia we Wrocławiu', 7),
       ('Jaworzno', '43-603', 'Fortuna 81', 'I Liceum Ogólnoksształczące im. Adama Mickiewicza w Jaworznie', 8),
       (null, null, null, 'Majowe Osiedle - zarząd', 9),
       (null, null, null, 'VBoard - zespół deweloperski', 10);
commit;

insert into db_vboard.board (board_id, created_by, created_date, last_modified_by, last_modified_date, accept_all,
                             address_city, address_post_code, address_street, board_name, description, is_private)
values (1, '1', '2021-05-14 19:37:52.553000', '1', '2021-05-14 19:37:52.553000', false, null, null, null,
        'Wyjazd nad Jezioro', null, true),
       (2, '1', '2021-05-14 19:39:55.665000', '1', '2021-05-14 19:39:55.665000', false, 'Wrocław', '53-123',
        'Jesienna 12/8', 'Współlokatorzy - Jesienna 12/8',
        'Tablica dla wynajmujących pokoje w mieszkaniu przy Jesiennej 12 m.8. Do kontaktu między wami i do zamieszczania informacji od właścicieli.',
        true),
       (3, '1', '2021-05-14 19:42:12.459000', '1', '2021-05-14 19:44:55.530000', true, null, null, null,
        'VBoard - aktualności i nowości',
        'Tablica prowadzona przez zespół VBoard. Ogłaszane na niej są nadchodzące nowe funkcje, oraz ewentualne planowane przerwy w działaniu. Jeżeli masz jakieś pytania, śmiało możesz zadać je tutaj!',
        false),
       (31, '7', '2021-05-17 23:04:10.040000', '7', '2021-05-17 23:04:22.848000', false, 'Wrocław', '53-321',
        'Grzybowa 57', 'Ogłoszenia dla uczniów - PSM I st. we Wrocławiu',
        'Na tej tablicy będą umieszczane wszelkie ogłoszenia przeznaczone dla uczniów naszej szkoły muzycznej - wszelkie informacje o zastępstwach, przełożonych zajęciach czy innych ważnych wydarzeniach.',
        false),
       (32, '8', '2021-05-17 23:08:12.914000', '8', '2021-05-17 23:08:21.911000', false, 'Jaworzno', '43-603',
        'Fortuna 81', 'I LO im. Adama Mickiewicza - Tablica ogłoszeń',
        'Na tej tablicy ogłoszeń będą pojawiały się ogłoszenia dla uczniów naszego LO - informacje o zastępstwach, skróconych lekcjach, apelach oraz innych wydarzeniach w życiu naszej szkoły.',
        false),
       (33, '9', '2021-05-17 23:10:44.097000', '9', '2021-05-17 23:10:52.084000', false, 'Wrocław', '53-123',
        'Kwietna 2-12', 'Majowe Osiedle - mieszkańcy',
        'Tablica ogłoszeń dla mieszkańców "Majowego Osiedla". Wszelkie informacje od zarządu osiedla, a także miejsce do komunikacji między mieszkańcami!',
        false);
commit;

insert into db_vboard.board_member (board_id, user_id, created_by, created_date, last_modified_by, last_modified_date,
                                    did_left, is_admin, order_index, want_notifications)
values (1, 1, '1', '2021-05-14 19:37:52.602000', '1', '2021-05-14 19:37:52.602000', false, true, 2, false),
       (1, 2, '1', '2021-05-14 19:49:50.877000', '1', '2021-05-14 19:49:50.877000', false, false, 0, false),
       (1, 3, '1', '2021-05-14 19:54:21.769000', '1', '2021-05-14 19:54:21.769000', false, false, 0, false),
       (1, 4, '1', '2021-05-14 19:57:30.642000', '1', '2021-05-14 19:57:30.642000', false, false, 0, false),
       (1, 5, '1', '2021-05-17 22:52:46.913000', '1', '2021-05-17 22:52:46.913000', false, false, 0, false),
       (1, 6, '1', '2021-05-17 22:55:20.050000', '1', '2021-05-17 22:55:20.050000', false, false, 0, false),
       (2, 1, '1', '2021-05-14 19:39:55.671000', '1', '2021-05-14 19:39:55.671000', false, true, 1, false),
       (2, 3, '1', '2021-05-17 22:58:39.146000', '1', '2021-05-17 23:00:28.609000', false, true, 2, false),
       (2, 4, '1', '2021-05-17 23:00:20.841000', '1', '2021-05-17 23:00:30.772000', false, true, 1, false),
       (3, 1, '1', '2021-05-14 19:42:12.465000', '1', '2021-05-14 19:42:12.465000', false, true, 4, false),
       (3, 2, '2', '2021-05-17 22:57:36.582000', '2', '2021-05-17 22:57:36.582000', false, false, 1, false),
       (3, 3, '3', '2021-05-17 22:56:58.918000', '3', '2021-05-17 22:56:58.918000', false, false, 1, false),
       (3, 5, '5', '2021-05-17 22:57:20.473000', '5', '2021-05-17 22:57:20.473000', false, false, 1, false),
       (3, 6, '6', '2021-05-17 22:56:37.509000', '6', '2021-05-17 22:56:37.509000', false, false, 1, false),
       (3, 7, '7', '2021-05-17 23:05:07.405000', '7', '2021-05-17 23:05:07.405000', false, false, 1, false),
       (3, 8, '8', '2021-05-17 23:06:48.185000', '8', '2021-05-17 23:06:48.185000', false, false, 1, false),
       (3, 10, '1', '2021-05-14 19:44:42.962000', '1', '2021-05-14 19:44:49.013000', false, true, 0, false),
       (31, 7, '7', '2021-05-17 23:04:10.046000', '7', '2021-05-17 23:04:10.046000', false, true, 0, false),
       (32, 1, '8', '2021-05-17 23:08:52.438000', '8', '2021-05-17 23:08:52.438000', false, false, 3, false),
       (32, 8, '8', '2021-05-17 23:08:12.919000', '8', '2021-05-17 23:08:12.919000', false, true, 0, false),
       (33, 1, '9', '2021-05-17 23:11:10.697000', '9', '2021-05-17 23:11:10.697000', false, false, 0, false),
       (33, 9, '9', '2021-05-17 23:10:44.102000', '9', '2021-05-17 23:10:44.102000', false, true, 0, false);
commit;

insert into db_vboard.post (post_id, created_by, created_date, last_modified_by, last_modified_date, is_pinned,
                            post_text, board_id, user_id)
values (4, '10', '2021-05-14 19:46:46.393000', '3', '2021-05-17 22:57:02.249000', true, 'Cześć! 👋
To nasze pierwsze ogłoszenie w serwisie VBoard!
Mamy nadzieję że stworzycie (lub dołączycie do) wielu przydatnych dla was tablic!', 3, 10),
       (5, '1', '2021-05-14 19:51:08.495000', '5', '2021-05-17 22:53:02.280000', false,
        'Na tej tablicy będziemy wrzucać ogólne info dla wszystkich jadących z nami na wyjazd. Wrzucajcie istotne info tutaj, to nie zginie na konwersacji. 😏',
        1, 1),
       (6, '2', '2021-05-14 19:52:05.485000', '6', '2021-05-17 22:55:54.594000', true, '‼️Uwaga‼️
Mam jeszcze jedno miejsce w aucie, jak ktoś chce ze mną jechać, to proszę o zgłoszenie się w komentarzu ;)', 1, 2),
       (11, '4', '2021-05-14 19:58:09.624000', '6', '2021-05-17 22:55:31.861000', true,
        'Mam brać ze sobą gitarę? Można coś pograć przy ognisku zawsze 🎸', 1, 4),
       (12, '1', '2021-05-14 20:00:37.714000', '6', '2021-05-17 22:56:26.015000', false, 'To lista jedzenia, które będę zabierał ze sobą na wyjazd jak coś :)
- 1 Keczup ostry jalapeno
- Cola 8x1.75 litra
- Woda mineralna 13 sztuk
- 4 soki pomarańczowe
- 2 pizze mrożone XD
- 2 chleby tostowe
- 1 Sos amerykański
- 1 ostry keczup
- 2 łagodne keczupy
- 36 karkówek
- 41 kiełbas zwykłych
- 12 białych kiełbas
- 8 chlebów krojonych z biedry', 1, 1),
       (28, '1', '2021-05-17 22:59:39.764000', '4', '2021-05-17 23:00:39.080000', false,
        'Jak coś to ja przez przypadek zabrałem klucz od skrzynki ze sobą. Mam nadzieję że wytrzymacie do poniedziałku bez niego 😅',
        2, 1),
       (30, '4', '2021-05-17 23:02:23.598000', '4', '2021-05-17 23:02:25.958000', true, '‼️UWAGA‼️
Dostałem info, że nie będzie w piątek cały dzień gazu, bo coś remontują!
Także żeby ktoś się nie zdziwił, że kuchenka nie działa :D', 2, 4),
       (34, '8', '2021-05-18 13:53:38.377000', '8', '2021-05-18 13:53:38.377000', false, 'Drodzy uczniowie!
Witamy was na naszej tablicy ogłoszeń :D', 32, 8),
       (35, '8', '2021-05-18 14:00:30.924000', '8', '2021-05-18 14:00:34.721000', true, '‼️ZASTĘPSTWA‼️ - środa (19.05.2021)

p. Jaroszewska:
1 lek. - ___
2 lek. - ___
3 lek. - 3c p. Kowalska chemia
4 lek. - 3c p. Kowalska chemia
5 lek. - 1c p. Zieleńska
6 lek. - 3w p. Jurewicz
7 lek. - 3biol do domu
8 lek. - 3biol do domu

p. Kruszewski:
1 lek. - ___
2 lek. - ___
3 lek. - ___
4 lek. - ___
5 lek. - ___
6 lek. - ___
7 lek. - 2b do domu
8 lek. - 2b do domu

Sala 6:
1 lek. - ___
2 lek. - sala 5
3 lek. - sala 5
4 lek. - sala 1a
5 lek. - sala 1a
6 lek. - sala 1a
7 lek. - sala 1a
8 lek. - ___', 32, 8),
       (36, '9', '2021-05-18 14:07:58.464000', '9', '2021-05-18 14:08:00.498000', true, 'Drodzy mieszkańcy,
W przyszłym tygodniu rozpoczniemy proces odkomarzania na naszym osiedlu. Wykonujemy go w związku z przyjętą ostatnio uchwałą zarządu osiedla - decyzja została podjęta po dokładnym zbadaniu warunków, na wniosek dużej części mieszkańców.

Uspokajamy, że procedura jest całkowicie bezpieczna dla ludzi oraz zwierząt domowych.', 33, 9);
commit;

insert into db_vboard.post_comment (comment_id, created_by, created_date, last_modified_by, last_modified_date,
                                    comment_text, board_id, user_id, post_id)
values (7, '3', '2021-05-14 19:54:47.437000', '3', '2021-05-14 19:54:47.437000', 'Ja byłabym chętna ✋', 1, 3, 6),
       (8, '1', '2021-05-14 19:55:14.390000', '1', '2021-05-14 19:55:14.390000',
        'To mam ci nie kupować blietu na pociąg w takim razie?', 1, 1, 6),
       (10, '3', '2021-05-14 19:55:50.717000', '3', '2021-05-14 19:55:50.717000',
        'Wychodzi, że nie. Niech tylko Adam potwierdzi :)', 1, 3, 6),
       (13, '4', '2021-05-14 20:01:05.351000', '4', '2021-05-14 20:01:05.351000', 'Mmmmm będzie grill 🍖', 1, 4, 12),
       (24, '5', '2021-05-17 22:53:26.619000', '5', '2021-05-17 22:53:26.619000',
        'Słyszałam, że Karolina też chciałaby jechać autem jak coś 🚗', 1, 5, 6),
       (25, '1', '2021-05-17 22:54:27.816000', '1', '2021-05-17 22:54:27.816000',
        'Oo no to super, przekażę jej info to się najwyżej skontaktuje już z Adamem :D', 1, 1, 6),
       (26, '5', '2021-05-17 22:54:36.694000', '5', '2021-05-17 22:54:36.694000', 'Ekstra ;)', 1, 5, 6),
       (27, '6', '2021-05-17 22:55:54.587000', '6', '2021-05-17 22:55:54.587000',
        'Jak coś to w mojej Sabince™️ też się znajdzie miejsce :D', 1, 6, 6),
       (29, '3', '2021-05-17 22:59:52.853000', '3', '2021-05-17 22:59:52.853000', 'Luz, nie ma problemu :D', 2, 3, 28);
commit;

insert into db_vboard.post_like (board_id, post_id, user_id, created_by, created_date, last_modified_by,
                                 last_modified_date)
values (1, 5, 2, '2', '2021-05-14 19:51:24.181000', '2', '2021-05-14 19:51:24.181000'),
       (1, 5, 3, '3', '2021-05-14 19:54:28.720000', '3', '2021-05-14 19:54:28.720000'),
       (1, 5, 4, '4', '2021-05-14 19:57:39.923000', '4', '2021-05-14 19:57:39.923000'),
       (1, 5, 5, '5', '2021-05-17 22:53:02.275000', '5', '2021-05-17 22:53:02.275000'),
       (1, 6, 1, '1', '2021-05-14 19:52:47.995000', '1', '2021-05-14 19:52:47.995000'),
       (1, 6, 5, '5', '2021-05-17 22:52:56.773000', '5', '2021-05-17 22:52:56.773000'),
       (1, 6, 6, '6', '2021-05-17 22:55:35.031000', '6', '2021-05-17 22:55:35.031000'),
       (1, 11, 1, '1', '2021-05-14 20:15:25.130000', '1', '2021-05-14 20:15:25.130000'),
       (1, 11, 5, '5', '2021-05-17 22:52:55.485000', '5', '2021-05-17 22:52:55.485000'),
       (1, 11, 6, '6', '2021-05-17 22:55:31.855000', '6', '2021-05-17 22:55:31.855000'),
       (1, 12, 4, '4', '2021-05-14 20:00:48.461000', '4', '2021-05-14 20:00:48.461000'),
       (1, 12, 5, '5', '2021-05-17 22:52:58.764000', '5', '2021-05-17 22:52:58.764000'),
       (1, 12, 6, '6', '2021-05-17 22:56:26.010000', '6', '2021-05-17 22:56:26.010000'),
       (2, 28, 3, '3', '2021-05-17 22:59:53.454000', '3', '2021-05-17 22:59:53.454000'),
       (2, 28, 4, '4', '2021-05-17 23:00:39.076000', '4', '2021-05-17 23:00:39.076000'),
       (3, 4, 1, '1', '2021-05-14 19:47:11.476000', '1', '2021-05-14 19:47:11.476000'),
       (3, 4, 3, '3', '2021-05-17 22:57:02.245000', '3', '2021-05-17 22:57:02.245000');
commit;

update db_vboard.hibernate_sequence
set next_val = 37;
commit;
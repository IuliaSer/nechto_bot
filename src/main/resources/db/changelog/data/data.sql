INSERT INTO USERS(name, username, password, authority)
values ('Alex', 'agertha', '$2a$12$esDECVwtLvuW/cBpp88hp.KzbfDYkvEfnk4g.gOZH7cLdIE6r5X5a', 'ROLE_OWNER');

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, false, 'ROLE_ADMIN'),
(2, true, 'dasha');

INSERT INTO acl_class (id, class) VALUES
(1, 'nechto.dto.AclGameDto');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, '1', null, 1, false);

INSERT INTO acl_entry (id, ace_order, acl_object_identity, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 2, true, true, true);

-- COPY users(name, username)
-- FROM '/Users/iulia_serzantova/IdeaProjects/Нечто/src/main/resources/Нечто.csv'
-- DELIMITER ','
-- CSV HEADER;

INSERT INTO USERS(id, name, username, password, authority)
values (6666777889, 'Alex', 'agertha', null, 'ROLE_OWNER');

INSERT INTO USERS(id, name, username, password, authority)
values (1399592491, 'Юля', 'iserzhan', null, 'ROLE_ADMIN');

INSERT INTO USERS(id, name, username, password, authority)
values (1399592492, 'Андрей', 'muteria', null, 'ROLE_USER');

INSERT INTO USERS(name, username, password, authority)
values (1399592493, 'Никита', 'kossmali', null, 'ROLE_USER');

INSERT INTO USERS(id, name, username, password, authority)
values (1399592494, 'Савелий', 'sallilam', null, 'ROLE_USER');

INSERT INTO USERS(id, name, username, password, authority)
values (1399592495, 'Вася', 'stoneild', null, 'ROLE_USER');

INSERT INTO USERS(name, username, password, authority)
values (1399592496, 'Венера', 'milana', null, 'ROLE_USER');

INSERT INTO USERS(name, username, password, authority)
values (1399592497, 'Аня', 'anya', null, 'ROLE_USER');

INSERT INTO USERS(name, username, password, authority)
values (1399592498, 'Дима', 'dima', null, 'ROLE_USER');

INSERT INTO USERS(name, username, password, authority)
values (1399592499, 'Ксения', 'ksenia', null, 'ROLE_USER');

INSERT INTO USERS(name, username, password, authority)
values (1399592410, 'Боб', 'wilfredo', null, 'ROLE_USER');

INSERT INTO USERS(name, username, password, authority)
values (1399592411, 'Саша', 'bookehyb', null, 'ROLE_USER');
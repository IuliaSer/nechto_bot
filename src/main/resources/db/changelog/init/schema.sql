create schema nechto;

CREATE TABLE if not exists GAMES (
                        id BIGSERIAL NOT NULL,
                        date TIMESTAMP,
                        PRIMARY KEY (id)
);

CREATE TABLE if not exists nechto.USERS (
                      id BIGSERIAL NOT NULL,
                      name varchar(255),
                      username varchar(255) NOT NULL,
                      password varchar(255),
                      authority varchar(255),
                      PRIMARY KEY (id),
                      UNIQUE(name, username)
);

CREATE TABLE if not exists SCORES (
                        id BIGSERIAL NOT NULL,
                        user_id int,
                        game_id int,
                        scores numeric default 0,
                        PRIMARY KEY (id),
                        UNIQUE(user_id, game_id),
                        FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
                        FOREIGN KEY (game_id) REFERENCES GAMES(id) ON DELETE CASCADE
);

create table if not exists SCORES_STATUS (
                                  scores_id int not null,
                                  status_id varchar(255) not null,
                                  FOREIGN KEY (scores_id) REFERENCES SCORES(id) ON DELETE CASCADE
);

create table acl_sid(
                        id bigint not null primary key,
                        principal boolean not null,
                        sid varchar(100) not null,
                        constraint unique_uk_1 unique(sid,principal)
);

CREATE TABLE acl_class (
                           id BIGINT NOT NULL PRIMARY KEY,
                           class VARCHAR(255) NOT NULL,
                           constraint unique_uk_2 unique(class)
);

CREATE TABLE acl_object_identity (
                                     id BIGINT NOT NULL PRIMARY KEY,
                                     object_id_class BIGINT NOT NULL,
                                     object_id_identity varchar(36) NOT NULL,
                                     parent_object bigint,
                                     owner_sid bigint,
                                     entries_inheriting BOOLEAN NOT NULL,
                                     constraint foreign_fk_1 foreign key(parent_object)references acl_object_identity(id),
                                     constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
                                     constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id));

CREATE TABLE acl_entry (
                           id BIGINT NOT NULL PRIMARY KEY,
                           acl_object_identity BIGINT NOT NULL,
                           ace_order int not null,
                           sid BIGINT NOT NULL,
                           mask INT NOT NULL,
                           granting BOOLEAN NOT NULL,
                           audit_success BOOLEAN NOT NULL,
                           audit_failure BOOLEAN NOT NULL,
                           constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
                           constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);
COMMIT;

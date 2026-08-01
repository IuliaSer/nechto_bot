create schema if not exists nechto;

CREATE TABLE if not exists nechto.USERS (
                      id BIGSERIAL NOT NULL,
                      name varchar(255),
                      username varchar(255) NOT NULL,
                      authority varchar(255) NOT NULL DEFAULT 'ROLE_USER',
                      PRIMARY KEY (id),
                      UNIQUE(name, username)
);

CREATE TABLE if not exists nechto.TABLES (
                                             id BIGSERIAL NOT NULL,
                                             name varchar(255),
                                             admin_id BIGSERIAL,
                                             date TIMESTAMP,

                                             PRIMARY KEY (id),
                                             FOREIGN KEY (admin_id) REFERENCES USERS(id) ON DELETE CASCADE
);

CREATE TABLE if not exists nechto.GAMES (
                                            id BIGSERIAL NOT NULL,
                                            date TIMESTAMP,
                                            table_id BIGSERIAL NOT NULL,

                                            PRIMARY KEY (id),
                                            FOREIGN KEY (table_id) REFERENCES TABLES(id) ON DELETE CASCADE
);

CREATE TABLE if not exists nechto.SCORES (
                        id BIGSERIAL NOT NULL,
                        user_id BIGSERIAL,
                        game_id BIGSERIAL,
                        scores numeric default 0,
                        PRIMARY KEY (id),
                        UNIQUE(user_id, game_id),
                        FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
                        FOREIGN KEY (game_id) REFERENCES GAMES(id) ON DELETE CASCADE
);

create table if not exists nechto.SCORES_STATUS (
                                  scores_id BIGSERIAL not null,
                                  status_id varchar(255) not null,
                                  FOREIGN KEY (scores_id) REFERENCES SCORES(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS nechto.TABLE_USERS (
                                                  table_id BIGINT NOT NULL,
                                                  user_id BIGINT NOT NULL,

                                                  PRIMARY KEY (table_id, user_id),

                                                  FOREIGN KEY (table_id) REFERENCES nechto.tables(id) ON DELETE CASCADE,
                                                  FOREIGN KEY (user_id) REFERENCES nechto.users(id) ON DELETE CASCADE
);

COMMIT;

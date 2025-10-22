create schema if not exists nechto;

CREATE TABLE if not exists GAMES (
                        id BIGSERIAL NOT NULL,
                        date TIMESTAMP,
                        PRIMARY KEY (id)
);

CREATE TABLE if not exists USERS (
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
                        user_id BIGSERIAL,
                        game_id BIGSERIAL,
                        scores numeric default 0,
                        PRIMARY KEY (id),
                        UNIQUE(user_id, game_id),
                        FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
                        FOREIGN KEY (game_id) REFERENCES GAMES(id) ON DELETE CASCADE
);

create table if not exists SCORES_STATUS (
                                  scores_id BIGSERIAL not null,
                                  status_id varchar(255) not null,
                                  FOREIGN KEY (scores_id) REFERENCES SCORES(id) ON DELETE CASCADE
);

COMMIT;

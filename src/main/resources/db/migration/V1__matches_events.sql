CREATE TABLE pong_watch.matches (
    "id" SERIAL,
    "name" VARCHAR(50) NOT NULL,
    "phone" VARCHAR(10),
    "location" VARCHAR(50),
    "completed" TIMESTAMP,
    "created" TIMESTAMP NOT NULL,
    "updated" TIMESTAMP NOT NULL,
    PRIMARY KEY("id")
);

CREATE TABLE pong_watch.motion_events (
    "id" SERIAL,
    "timestamp" TIMESTAMP NOT NULL,
    "motion_detected" SMALLINT NOT NULL,
    PRIMARY KEY ("id")
);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA pong_watch TO ping;
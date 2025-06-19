CREATE TABLE IF NOT EXISTS role
(
    id    INTEGER PRIMARY KEY,
    title varchar(30)
);

CREATE TABLE IF NOT EXISTS "user"
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email        varchar,
    password     varchar,
    photo_url    varchar,
    role_id      INTEGER REFERENCES role (id)
);

CREATE TABLE IF NOT EXISTS product
(
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title       varchar(50),
    description varchar(500),
    price       decimal,
    type        varchar(30),
    photo_url   varchar
);

CREATE TABLE IF NOT EXISTS "order"
(
    id            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          varchar(30),
    email         varchar,
    phone_number  varchar(20),
    address       varchar,
    comment       varchar,
    delivery_time timestamp,
    status        varchar,
    timestamp     timestamp,
    product_id       INTEGER REFERENCES product (id)
);

CREATE TABLE gift_certificate
(
    id               BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    name             VARCHAR(255)       NULL,
    `description`    VARCHAR(255)       NULL,
    price            DECIMAL            NULL,
    duration         DOUBLE             NULL,
    create_date      datetime           NULL DEFAULT CURRENT_TIMESTAMP,
    last_update_date datetime           NULL DEFAULT CURRENT_TIMESTAMP,
    is_available     BIT(1)             NULL
);

create table gift_certificate_audit
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    certificate_id  BIGINT NOT NULL,
    operation_date  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    operation_type  VARCHAR(10)
);

CREATE TABLE tag
(
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    name         VARCHAR(255)       NULL
);

create table tag_audit
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    tag_id          BIGINT NOT NULL,
    operation_date  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    operation_type  VARCHAR(10)
);

create table tag_certificate (
     certificate_id bigint not null,
     tag_id bigint not null,
     foreign key (certificate_id) references gift_certificate (id) on UPDATE CASCADE ON DELETE CASCADE,
     foreign key (tag_id) references tag (id) on UPDATE CASCADE ON DELETE RESTRICT,
     PRIMARY KEY (certificate_id, tag_id)
);

CREATE TABLE user
(
    id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    username    VARCHAR(255)       NULL,
    balance     DECIMAL            NULL
);

create table user_audit
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    operation_date  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    operation_type  VARCHAR(10)
);

CREATE TABLE `order`
(
    id             BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    total_price    DECIMAL            NULL,
    buy_date       datetime           NULL,
    user_id        BIGINT                NULL,
    certificate_id BIGINT                NULL,
    foreign key (user_id) references user(id),
    foreign key (certificate_id) references gift_certificate(id)
);

create table order_audit
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    order_id        BIGINT NOT NULL,
    operation_date  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    operation_type  VARCHAR(10)
);

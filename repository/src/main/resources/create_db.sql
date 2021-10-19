CREATE TABLE gift_certificate
(
    id               INT AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    name             VARCHAR(255)       NULL,
    `description`    VARCHAR(255)       NULL,
    price            DECIMAL            NULL,
    duration         DOUBLE             NULL,
    create_date      datetime           NULL,
    last_update_date datetime           NULL,
    is_available     BIT(1)             NULL,
);

CREATE TABLE tag
(
    id           INT AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    name         VARCHAR(255)       NULL,
    is_available BIT(1)             NULL
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
    id          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    username    VARCHAR(255)       NULL,
    balance     DECIMAL            NULL,
    update_date datetime           NULL,
    is_active   BIT(1)             NULL);

CREATE TABLE `order`
(
    id             INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    total_price    DECIMAL            NULL,
    buy_date       datetime           NULL,
    user_id        INT                NULL,
    certificate_id INT                NULL,
    is_available   BIT(1)             NULL,
    foreign key (user_id) references user(id),
    foreign key (certificate_id) references gift_certificate(id)
);

ALTER TABLE `order`
    ADD CONSTRAINT FK_ORDER_ON_CERTIFICATE FOREIGN KEY (certificate_id) REFERENCES gift_certificate (id);

ALTER TABLE `order`
    ADD CONSTRAINT FK_ORDER_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

create table gift_certificate (
      id BIGINT auto_increment NOT NULL PRIMARY KEY,
      name varchar(20),
      description varchar(255),
      price DECIMAL(10),
      duration FLOAT,
      create_date DATETIME,
      last_update_date DATETIME);

create table tag (
     id BIGINT auto_increment not null primary key,
     name varchar(20)
);

create table tag_certificate (
     certificate_id bigint not null,
     tag_id bigint not null,
     foreign key (certificate_id) references gift_certificate (id) on UPDATE CASCADE ON DELETE RESTRICT,
     foreign key (tag_id) references tag (id) on UPDATE CASCADE ON DELETE RESTRICT,
     PRIMARY KEY (certificate_id, tag_id)
);
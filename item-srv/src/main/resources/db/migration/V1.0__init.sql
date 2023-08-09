create sequence currency_id_seq start with 1 increment by 1;
create sequence hierarchy_id_seq start with 1 increment by 1;
create sequence item_barcode_id_seq start with 1 increment by 5000;
create sequence item_price_value_id_seq start with 1 increment by 5000;
create sequence item_id_seq start with 1 increment by 5000;
create sequence price_list_id_seq start with 1 increment by 1;

create table currency
(
    id          bigint                      not null DEFAULT nextval('currency_id_seq') primary key,
    code        varchar(50)                 not null unique,
    name        varchar(50),
    description varchar(255),
    is_deleted  char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date timestamp(6) with time zone not null,
    update_date timestamp(6) with time zone not null,
    version     bigint                      not null
);

create table hierarchy
(
    id          bigint                      not null DEFAULT nextval('hierarchy_id_seq') primary key,
    code        varchar(50)                 not null unique,
    name        varchar(50),
    parent_id   bigint,
    is_deleted  char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date timestamp(6) with time zone not null DEFAULT current_timestamp,
    update_date timestamp(6) with time zone not null DEFAULT current_timestamp,
    version     bigint                      not null
);
create table item
(
    id           bigint                      not null DEFAULT nextval('item_id_seq') primary key,
    code         varchar(50)                 not null unique,
    name         varchar(50),
    hierarchy_id bigint,
    is_deleted   char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date  timestamp(6) with time zone not null DEFAULT current_timestamp,
    update_date  timestamp(6) with time zone not null DEFAULT current_timestamp,
    version      bigint                      not null
);
create table price_list
(
    id          bigint                      not null DEFAULT nextval('price_list_id_seq') primary key,
    code        varchar(50)                 not null unique,
    description varchar(255),
    currency_id bigint not null,
    is_deleted  char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date timestamp(6) with time zone not null DEFAULT current_timestamp,
    update_date timestamp(6) with time zone not null DEFAULT current_timestamp,
    version     bigint                      not null
);
create table item_barcode
(
    id          bigint                      not null DEFAULT nextval('item_barcode_id_seq') primary key,
    item_id     bigint                      not null,
    barcode     varchar(50)                 not null,
    description varchar(255),
    is_default  char(1)                     not null check (is_deleted in ('N', 'Y')),
    is_deleted  char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date timestamp(6) with time zone not null DEFAULT current_timestamp,
    update_date timestamp(6) with time zone not null DEFAULT current_timestamp,
    version     bigint                      not null
);
create table item_price_value
(
    id            bigint                      not null DEFAULT nextval('item_price_value_id_seq') primary key,
    item_id       bigint                      not null,
    price_list_id bigint                      not null,
    value         numeric(38, 2)              not null,
    start_time    timestamp(6) with time zone not null,
    is_deleted    char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date   timestamp(6) with time zone not null DEFAULT current_timestamp,
    update_date   timestamp(6) with time zone not null DEFAULT current_timestamp,
    version       bigint                      not null
);

alter table if exists hierarchy
    add constraint FK_hierarchy_parent_id foreign key (parent_id) references hierarchy;
alter table if exists item
    add constraint FK_item_hierarchy_id foreign key (hierarchy_id) references hierarchy;
alter table if exists item_barcode
    add constraint FK_barcode_item_id foreign key (item_id) references item;
alter table if exists price_list
    add constraint FK_price_list_currency_id foreign key (currency_id) references currency;
alter table if exists item_price_value
    add constraint FK_item_price_value_item_id foreign key (item_id) references item;
alter table if exists item_price_value
    add constraint FK_item_price_value_price_list_id foreign key (price_list_id) references price_list;

insert into currency (code, name, description, is_deleted, create_date, update_date, version)
values ('₴', 'UAH', 'National currency of Ukraine', 'N', '2023-07-01 21:27:02.214000 +00:00', '2023-07-01 21:27:02.214000 +00:00', 1),
       ('₽', 'RUB', 'National currency of Russia', 'N', '2023-07-01 21:27:02.214000 +00:00', '2023-07-01 21:27:02.214000 +00:00', 1),
       ('$', 'USD', 'National currency of United State of America', 'N', '2023-07-01 21:27:02.214000 +00:00', '2023-07-01 21:27:02.214000 +00:00', 1);

insert into price_list (is_deleted, create_date, id, update_date, version, code, currency_id)
values ('N', '2023-07-01 21:26:58.836000 +00:00', 1, '2023-07-01 21:27:02.214000 +00:00', 1, 'Purchasing_USD', 3),
       ('N', '2023-07-01 21:27:42.321000 +00:00', 2, '2023-07-01 21:27:38.739000 +00:00', 1, 'Selling_USD', 3),
       ('N', '2023-07-01 21:27:42.321000 +00:00', 3, '2023-07-01 21:27:38.739000 +00:00', 1, 'Purchasing_RUB', 1),
       ('N', '2023-07-01 21:27:42.321000 +00:00', 4, '2023-07-01 21:27:38.739000 +00:00', 1, 'Selling_RUB', 1),
       ('N', '2023-07-01 21:27:42.321000 +00:00', 5, '2023-07-01 21:27:38.739000 +00:00', 1, 'Purchasing_UAH', 1),
       ('N', '2023-07-01 21:27:42.321000 +00:00', 6, '2023-07-01 21:27:38.739000 +00:00', 1, 'Selling_UAH', 2);
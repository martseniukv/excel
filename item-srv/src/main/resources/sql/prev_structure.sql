create sequence hierarchy_seq start with 1 increment by 50
create sequence item_barcode_seq start with 1 increment by 50
create sequence item_price_value_seq start with 1 increment by 50
create sequence item_seq start with 1 increment by 50
create sequence price_list_seq start with 1 increment by 50
create sequence price_number_seq start with 1 increment by 50
create table hierarchy
(
    is_deleted  char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date timestamp(6) with time zone not null,
    id          bigint                      not null,
    parent_id   bigint unique,
    update_date timestamp(6) with time zone not null,
    version     bigint                      not null,
    code        varchar(50)                 not null unique,
    name        varchar(50),
    primary key (id)
)
create table item
(
    is_deleted   char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date  timestamp(6) with time zone not null,
    hierarchy_id bigint,
    id           bigint                      not null,
    update_date  timestamp(6) with time zone not null,
    version      bigint                      not null,
    code         varchar(50)                 not null unique,
    name         varchar(50),
    primary key (id)
)
create table item_barcode
(
    is_deleted  char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date timestamp(6) with time zone not null,
    id          bigint                      not null,
    item_id     bigint,
    update_date timestamp(6) with time zone not null,
    version     bigint                      not null,
    barcode     varchar(50)                 not null unique,
    description varchar(255),
    primary key (id)
)
create table item_price_value
(
    is_deleted      char(1)                     not null check (is_deleted in ('N', 'Y')),
    value           numeric(38, 2)              not null,
    create_date     timestamp(6) with time zone not null,
    from_date       timestamp(6) with time zone not null,
    id              bigint                      not null,
    item_id         bigint                      not null,
    price_number_id bigint                      not null,
    update_date     timestamp(6) with time zone not null,
    version         bigint                      not null,
    primary key (id)
)
create table price_list
(
    is_deleted  char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date timestamp(6) with time zone not null,
    id          bigint                      not null,
    update_date timestamp(6) with time zone not null,
    version     bigint                      not null,
    code        varchar(50)                 not null unique,
    primary key (id)
)
create table price_number
(
    is_deleted    char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date   timestamp(6) with time zone not null,
    id            bigint                      not null,
    price_list_id bigint,
    update_date   timestamp(6) with time zone not null,
    version       bigint                      not null,
    code          varchar(50)                 not null unique,
    primary key (id)
)
alter table if exists hierarchy
    add constraint FK_hierarchy_parent_id foreign key (parent_id) references hierarchy
alter table if exists item
    add constraint FK_item_hierarchy_id foreign key (hierarchy_id) references hierarchy
alter table if exists item_barcode
    add constraint FK_barcode_item_id foreign key (item_id) references item
alter table if exists item_price_value
    add constraint FK_item_price_value_item_id foreign key (item_id) references item
alter table if exists item_price_value
    add constraint FK_item_price_value_price_number_id foreign key (price_number_id) references price_number
alter table if exists price_number
    add constraint FK_price_number_price_list_id foreign key (price_list_id) references price_list


insert into hierarchy (is_deleted, create_date, id, parent_id, update_date, version, code, name)
values ('N', '2023-07-01 21:24:01.143000 +00:00', 1, null, '2023-07-01 21:24:06.085000 +00:00', 1, 'Hierarchy 1',
        'Hierarchy 1'),
       ('N', '2023-07-01 21:24:01.143000 +00:00', 2, 1, '2023-07-01 21:24:06.085000 +00:00', 1, 'Hierarchy 1.1',
        'Hierarchy 1.1'),
       ('N', '2023-07-01 21:24:01.143000 +00:00', 3, null, '2023-07-01 21:24:06.085000 +00:00', 1, 'Hierarchy 2',
        'Hierarchy 2'),
       ('N', '2023-07-01 21:24:01.143000 +00:00', 4, 3, '2023-07-01 21:24:06.085000 +00:00', 1, 'Hierarchy 2.1',
        'Hierarchy 2.1');

insert into price_list (is_deleted, create_date, id, update_date, version, code)
values ('N', '2023-07-01 21:26:58.836000 +00:00', 1, '2023-07-01 21:27:02.214000 +00:00', 1, 'Purchasing'),
       ('N', '2023-07-01 21:27:42.321000 +00:00', 2, '2023-07-01 21:27:38.739000 +00:00', 1, 'Selling');

insert into price_number (is_deleted, create_date, id, price_list_id, update_date, version, code)
values ('N', '2023-07-01 21:28:34.384000 +00:00', 1, 1, '2023-07-01 21:28:38.175000 +00:00', 1, 'Price Number 1.1'),
       ('N', '2023-07-01 21:28:34.384000 +00:00', 2, 1, '2023-07-01 21:28:38.175000 +00:00', 1, 'Price Number 1.2'),
       ('N', '2023-07-01 21:28:34.384000 +00:00', 3, 1, '2023-07-01 21:28:38.175000 +00:00', 2, 'Price Number 2.1'),
       ('N', '2023-07-01 21:28:34.384000 +00:00', 4, 1, '2023-07-01 21:28:38.175000 +00:00', 2, 'Price Number 2.2');

insert into item (is_deleted, create_date, hierarchy_id, id, update_date, version, code, name)
values ('N', '2023-07-01 21:30:16.614000 +00:00', 1, 1, '2023-07-01 21:30:22.440000 +00:00', 1, '001', 'T-Shirt'),
       ('N', '2023-07-01 21:30:16.614000 +00:00', 1, 2, '2023-07-01 21:30:22.440000 +00:00', 1, '002', 'Skirt'),
       ('N', '2023-07-01 21:30:16.614000 +00:00', 2, 3, '2023-07-01 21:30:22.440000 +00:00', 1, '003', 'Skirt Small'),
       ('N', '2023-07-01 21:30:16.614000 +00:00', 4, 4, '2023-07-01 21:30:22.440000 +00:00', 1, '004', 'Polo');

insert into item_barcode (is_deleted, create_date, id, item_id, update_date, version, barcode)
values ('N', '2023-07-01 21:33:37.256000 +00:00', 1, 1, '2023-07-01 21:33:40.496000 +00:00', 1, 'item_1_barcode_1'),
       ('N', '2023-07-01 21:33:37.256000 +00:00', 2, 1, '2023-07-01 21:33:40.496000 +00:00', 1, 'item_1_barcode_2'),
       ('N', '2023-07-01 21:33:37.256000 +00:00', 3, 2, '2023-07-01 21:33:40.496000 +00:00', 1, 'item_2_barcode_1'),
       ('N', '2023-07-01 21:33:37.256000 +00:00', 4, 2, '2023-07-01 21:33:40.496000 +00:00', 1, 'item_2_barcode_2');

insert into item_price_value (is_deleted, value, create_date, id, item_id, price_number_id, update_date, version,
                              from_date)
values ('N', 10.00, '2023-07-02 16:23:23.771000 +00:00', 1, 1, 1, '2023-07-02 16:23:30.701000 +00:00', 1,
        '2023-07-03 16:23:34.000000'),
       ('N', 20.00, '2023-07-02 16:23:23.771000 +00:00', 2, 1, 1, '2023-07-02 16:23:30.701000 +00:00', 1,
        '2023-07-05 16:23:34.000000'),
       ('N', 110.00, '2023-07-02 16:23:23.771000 +00:00', 3, 3, 1, '2023-07-02 16:23:30.701000 +00:00', 1,
        '2023-07-01 16:23:34.000000'),
       ('N', 100.00, '2023-07-02 16:23:23.771000 +00:00', 4, 3, 1, '2023-07-02 16:23:30.701000 +00:00', 1,
        '2023-07-02 16:23:34.000000');
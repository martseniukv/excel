create sequence import_task_id_seq start with 1 increment by 1;
create sequence import_error_id_seq start with 1 increment by 1;
create sequence import_sheet_detail_id_seq start with 1 increment by 1;

create table import_task
(
    id            bigint                      not null DEFAULT nextval('import_task_id_seq') primary key,
    file_name     varchar(50),
    start_time    timestamp(6) with time zone not null,
    import_type   varchar(50)                 not null,
    import_status varchar(50)                 not null,
    file          bytea,
    is_finished   char(1)                     not null check (is_deleted in ('N', 'Y')),
    is_deleted    char(1)                     not null check (is_deleted in ('N', 'Y')),
    create_date   timestamp(6) with time zone not null,
    update_date   timestamp(6) with time zone not null,
    version       bigint                      not null
);

create table import_sheet_detail
(
    id             bigint      not null DEFAULT nextval('import_sheet_detail_id_seq') primary key,
    import_task_id bigint      not null,
    sheet_name     varchar(50) not null,
    line_from      int         not null,
    line_to        int         not null,
    import_object  varchar(50) not null
);

create table import_error
(
    id              bigint       not null DEFAULT nextval('import_error_id_seq') primary key,
    sheet_detail_id bigint       not null,
    row_num         int          not null,
    column_num      int          not null,
    message         varchar(255) not null
);

alter table if exists import_sheet_detail
    add constraint FK_import_sheet_detail_import_task_id foreign key (import_task_id) references import_task;

alter table if exists import_error
    add constraint FK_import_error_sheet_detail_id foreign key (sheet_detail_id) references import_sheet_detail;


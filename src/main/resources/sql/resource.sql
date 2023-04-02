insert into resource (id, created_date, modified_date, url, page_type, resource_type, page_id)
values(1, '2022-01-01','2022-01-01', '/test1','Conference','IMAGE',1);

insert into resource (id, created_date, modified_date, url, page_type, resource_type, page_id, delete_at)
values(2, '2022-01-01','2022-01-01', '/test1','Study','IMAGE',1, CURRENT_TIMESTAMP)
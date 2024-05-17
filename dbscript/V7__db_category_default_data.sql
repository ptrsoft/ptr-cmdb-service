INSERT INTO public.db_category
("name", created_on, created_by, organization_id)
VALUES(upper('No SQL DB'), current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') );
INSERT INTO public.db_category
("name", created_on, created_by, organization_id)
VALUES(upper('SQL DB'), current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') );
INSERT INTO public.db_category
("name", created_on, created_by, organization_id)
VALUES(upper('Search DB'), current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') );
INSERT INTO public.db_category
("name", created_on, created_by, organization_id)
VALUES(upper('Ledger DB'), current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') );
INSERT INTO public.db_category
("name", created_on, created_by, organization_id)
VALUES(upper('Cache DB'), current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') );
INSERT INTO public.db_category
("name", created_on, created_by, organization_id)
VALUES(upper('Object DB'), current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') );
INSERT INTO public.db_category
("name", created_on, created_by, organization_id)
VALUES(upper('Git DB'), current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') );
INSERT INTO public.db_category
("name", created_on, created_by, organization_id)
VALUES(upper('Metrics DB'), current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') );
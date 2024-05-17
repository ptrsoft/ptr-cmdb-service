INSERT INTO public.config
("key", value, status, created_on, created_by, organization_id, is_encrypted)
VALUES('BATCH_JOB_PULL_AWS_ELEMENTS', 'DEACTIVE', 'ACTIVE', current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') , false);
INSERT INTO public.config
("key", value, status, created_on, created_by, organization_id, is_encrypted)
VALUES('BATCH_JOB_PULL_AWS_ELEMENTS_COST', 'DEACTIVE', 'ACTIVE', current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') , false);
INSERT INTO public.config
("key", value, status, created_on, created_by, organization_id, is_encrypted)
VALUES('BATCH_JOB_AWS_ELEMENTS_AUTO_TAG_PROCESS', 'DEACTIVE', 'ACTIVE', current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') , false);
INSERT INTO public.config
("key", status, created_on, created_by, organization_id, is_encrypted)
VALUES('GLOBAL_AWS_ACCESS_KEY', 'ACTIVE', current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') , true);
INSERT INTO public.config
("key", status, created_on, created_by, organization_id, is_encrypted)
VALUES('GLOBAL_AWS_SECRET_KEY', 'ACTIVE', current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') , true);
INSERT INTO public.config
("key", value, status, created_on, created_by, organization_id, is_encrypted)
VALUES('GLOBAL_AWS_REGION', 'us-east-1', 'ACTIVE', current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') , false);
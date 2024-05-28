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
("key", value, status, created_on, created_by, organization_id, is_encrypted)
VALUES('GLOBAL_AWS_ACCESS_KEY','+3gsjypzYmEjVH67Wn98JnkMwfn6wY4R3kmT83nclbo=', 'ACTIVE', current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') , true);
INSERT INTO public.config
("key", value, status, created_on, created_by, organization_id, is_encrypted)
VALUES('GLOBAL_AWS_SECRET_KEY', '5aiuCf0xyojTZVinNSYkHlOVPcmEhJM4hpg8LsNuTV5GrO4uoR9O5gsaxLR8+h2S', 'ACTIVE', current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') , true);
INSERT INTO public.config
("key", value, status, created_on, created_by, organization_id, is_encrypted)
VALUES('GLOBAL_AWS_REGION', 'us-east-1', 'ACTIVE', current_timestamp, 'System', (select id from organization o where name = 'SYNECTIKS') , false);
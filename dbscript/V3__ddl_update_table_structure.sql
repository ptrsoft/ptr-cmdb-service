alter table cloud_element rename column cloud_entity to cloud_identity;
alter table cloud_element drop column instance_id;
alter table cloud_element drop column arn;
alter table micro_service add column department_id int8;
alter table micro_service add column deployment_environment_id int8;
alter table organization add column security_service_org_id int8;
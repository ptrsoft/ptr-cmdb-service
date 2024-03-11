alter table cloud_element add column service_category varchar(255);
alter table cloud_element add column region varchar(255);
alter table cloud_element add column log_group varchar(255);
alter table product_enclave add column service_category varchar(255);
alter table product_enclave add column region varchar(255);
alter table product_enclave add column log_group varchar(255);
update cloud_element set service_category = 'COMPUTE' where element_type in ('EC2','LAMBDA','ECS', 'EKS','BATCH','FARGATE');
update cloud_element set service_category = 'STORAGE' where element_type in ('S3','EBS','EFS', 'GLACIER','STORAGE_GATEWAY');
update cloud_element set service_category = 'NETWORK' where element_type in ('VPC','ROUTE53','DIRECT_CONNECT', 'VPN','GLOBAL_ACCELERATOR');
update cloud_element set service_category = 'DATABASE' where element_type in ('RDS','AURORA','DYNAMODB', 'REDSHIFT','NEPTUNE','DOCUMENTDB');
update cloud_element set service_category = 'OTHER' where element_type not in ('EC2','LAMBDA','ECS', 'EKS','BATCH','FARGATE','S3','EBS','EFS', 'GLACIER','STORAGE_GATEWAY', 'VPC','ROUTE53','DIRECT_CONNECT', 'VPN','GLOBAL_ACCELERATOR','RDS','AURORA','DYNAMODB', 'REDSHIFT','NEPTUNE','DOCUMENTDB');
update product_enclave set service_category = 'NETWORK';


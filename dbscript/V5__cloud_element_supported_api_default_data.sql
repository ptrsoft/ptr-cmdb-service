INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'error-count-panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'cpu_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'AverageUsage,CurrentUsage,MaxUsage');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'storage_utilization_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'network_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'InboundTraffic,OutboundTraffic');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'ec2-config-data', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'memory_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'AverageUsage,CurrentUsage,MaxUsage');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'cpu_usage_user_panel', 'ACTIVE', current_timestamp, 'System', 'CPU_User');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'cpu_usage_sys_panel', 'ACTIVE', current_timestamp, 'System', 'CPU_Sys');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'cpu_usage_nice_panel', 'ACTIVE', current_timestamp, 'System', 'CPU_Nice');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'cpu_usage_idle_panel', 'ACTIVE', current_timestamp, 'System', 'CPU_Idle');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'mem_usage_free_panel', 'ACTIVE', current_timestamp, 'System', 'Mem_Free');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'mem_cached_panel', 'ACTIVE', current_timestamp, 'System', 'Mem_Cache');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'mem_usage_total_panel', 'ACTIVE', current_timestamp, 'System', 'Mem_Total');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'mem_usage_used_panel', 'ACTIVE', current_timestamp, 'System', 'Mem_Used');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'disk_writes_panel', 'ACTIVE', current_timestamp, 'System', 'Disk_Writes');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'disk_reads_panel', 'ACTIVE', current_timestamp, 'System', 'Disk_Reads');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'disk_available_panel', 'ACTIVE', current_timestamp, 'System', 'DiskAvailable');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'disk_used_panel', 'ACTIVE', current_timestamp, 'System', 'Disk_Used');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'net_inpackets_panel', 'ACTIVE', current_timestamp, 'System', 'Net_InPackets');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'net_inbytes_panel', 'ACTIVE', current_timestamp, 'System', 'Net_Inbytes');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'net_outbytes_panel', 'ACTIVE', current_timestamp, 'System', 'Net_Outbytes');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'net_outpackets_panel', 'ACTIVE', current_timestamp, 'System', 'Net_Outpackets');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'allocatable_cpu_panel', 'ACTIVE', current_timestamp, 'System', 'AllocatableCPU');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'cpu_limits_panel', 'ACTIVE', current_timestamp, 'System', 'Cpu limits');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'cpu_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'AverageUsage,CurrentUsage,MaxUsage');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'cpu_graph_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'CPU Utilization');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'cpu_requests_panel', 'ACTIVE', current_timestamp, 'System', 'CPU requests');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'cpu_node_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'CPU Utilization');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'memory_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'AverageUsage,CurrentUsage,MaxUsage');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'memory_limits_panel', 'ACTIVE', current_timestamp, 'System', 'Memory limits');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'memory_requests_panel', 'ACTIVE', current_timestamp, 'System', 'Memory requests');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'memory_usage_panel', 'ACTIVE', current_timestamp, 'System', 'Memory Usage');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'memory_graph_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'Memory utilization');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'network_availability_panel', 'ACTIVE', current_timestamp, 'System', 'Network Availability');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'network_in_out_panel', 'ACTIVE', current_timestamp, 'System', 'Network in and Network out');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'network_throughput_panel', 'ACTIVE', current_timestamp, 'System', 'NetworkIn,NetworkOut');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'network_throughput_single_panel', 'ACTIVE', current_timestamp, 'System', 'Network Throughput');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'network_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'DataTransferred,InboundTraffic,OutboundTraffic');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'node_downtime_panel', 'ACTIVE', current_timestamp, 'System', 'Node Downtime');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'node_uptime_panel', 'ACTIVE', current_timestamp, 'System', 'Node Uptime');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'node_event_logs_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'service_availability_panel', 'ACTIVE', current_timestamp, 'System', 'Service Availability');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'alerts_and_notifications_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'node_capacity_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'alert_and_notification_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'alert_and_notification_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'instance_status_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'hosted_services_overview_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'error_tracking_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'custom_alert_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'net_throughput_panel', 'ACTIVE', current_timestamp, 'System', 'NetworkThroughputData');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'instance_health_check_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'network_inbound_panel', 'ACTIVE', current_timestamp, 'System', 'NetworkInbound');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'network_outbound_panel', 'ACTIVE', current_timestamp, 'System', 'NetworkOutbound');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'error_rate_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'instance_stop_count_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'instance_stop_count_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'allocatable_memory_panel', 'ACTIVE', current_timestamp, 'System', 'AllocatableMemory');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'instance_running_hour_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'instance_hours_stopped_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'latency_panel', 'ACTIVE', current_timestamp, 'System', 'DataTransferred,InboundTraffic,OutboundTraffic');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'storage_utilization_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'allocatable_memory_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'disk_utilization_panel', 'ACTIVE', current_timestamp, 'System', 'DiskUtilization');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'node_condition_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'disk_io_performance_panel', 'ACTIVE', current_timestamp, 'System', 'TotalOps');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'node_stability_index_panel', 'ACTIVE', current_timestamp, 'System', 'Resource_utilization_patterns_panle');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'cpu_utilization_graph_panel', 'ACTIVE', current_timestamp, 'System', 'CPU Utilization');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'memory_utilization_graph_panel', 'ACTIVE', current_timestamp, 'System', 'Memory utilization');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'disk_io_panel', 'ACTIVE', current_timestamp, 'System', 'DiskReadBytes');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'node_failure_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'incident_response_time_panel', 'ACTIVE', current_timestamp, 'System', 'IncidentResponse');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'resource_utilization_patterns_panel', 'ACTIVE', current_timestamp, 'System', 'Resource Utilization Patterns');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'cpu_utilization_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'database_connections_panel', 'ACTIVE', current_timestamp, 'System', 'Database_Connections');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'index_size_panel', 'ACTIVE', current_timestamp, 'System', 'Index_Size');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'network_utilization_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'network_traffic_panel', 'ACTIVE', current_timestamp, 'System', 'Inbound Traffic,Outbound Traffic');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'freeable_memory_panel', 'ACTIVE', current_timestamp, 'System', 'FreeableMemory');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'free_storage_space_panel', 'ACTIVE', current_timestamp, 'System', 'FreeStorageSpace');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'disk_queue_depth_panel', 'ACTIVE', current_timestamp, 'System', 'DiskQueueDepth');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'replication_slot_disk_usage', 'ACTIVE', current_timestamp, 'System', 'ReplicationSlotDiskUsage');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'network_receive_throughput_panel', 'ACTIVE', current_timestamp, 'System', 'NetworkReceiveThroughput');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'network_transmit_throughput_panel', 'ACTIVE', current_timestamp, 'System', 'NetworkTransmitThroughput');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'concurrency_panel', 'ACTIVE', current_timestamp, 'System', 'concurrency');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'used_and_unused_memory_data_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'max_memory_used_panel', 'ACTIVE', current_timestamp, 'System', 'Max Memory Used (MB)');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'max_memory_used_graph_panel', 'ACTIVE', current_timestamp, 'System', 'Max Memory Used (MB)');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'cold_start_duration_panel', 'ACTIVE', current_timestamp, 'System', 'Cold Start Duration');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'throttles_panel', 'ACTIVE', current_timestamp, 'System', 'Throttling');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'network_utilization_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'alert_and_notification_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'cpu_credit_usage_panel', 'ACTIVE', current_timestamp, 'System', 'CPU_Credit_Usage');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'cpu_credit_balance_panel', 'ACTIVE', current_timestamp, 'System', 'CPU_Credit_Balance');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'cpu_surplus_credit_balance_panel', 'ACTIVE', current_timestamp, 'System', 'CPU_Surplus_Credit_Balance');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'cpu_surplus_credits_charged_panel', 'ACTIVE', current_timestamp, 'System', 'CPU_Surplus_Credit_Balance');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'cpu_utilization_graph_panel', 'ACTIVE', current_timestamp, 'System', 'CPU Utilization');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'database_workload_overview_panel', 'ACTIVE', current_timestamp, 'System', 'DBLoad');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'db_load_non_cpu_panel', 'ACTIVE', current_timestamp, 'System', 'DBLoadNonCPU');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'db_load_cpu_panel', 'ACTIVE', current_timestamp, 'System', 'DBLoadCPU');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'instance_health_check_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'iops_panel', 'ACTIVE', current_timestamp, 'System', 'Write,Read');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'execution_time_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'functions_by_region_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'storage_utilization_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'read_iops_panel', 'ACTIVE', current_timestamp, 'System', 'ReadIOPS');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'write_iops_panel', 'ACTIVE', current_timestamp, 'System', 'WriteIOPS');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'latency_analysis_panel', 'ACTIVE', current_timestamp, 'System', 'LatencyAnalysis');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'ECS', 'cpu_utilization_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'ECS', 'storage_utilization_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'transaction_logs_disk_usage_panel', 'ACTIVE', current_timestamp, 'System', 'Transaction_Logs_Disk_Usage');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'transaction_logs_generation_panel', 'ACTIVE', current_timestamp, 'System', 'Transaction_Logs_Generation');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'maintenance_schedule_overview_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'ECS', 'resource_created_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'success_and_failed_function_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'uptime_of_deployment_stages', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'successful_and_failed_events_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'successful_and_failed_events_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'cache_hit_count_panel', 'ACTIVE', current_timestamp, 'System', 'CacheHits');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'cache_miss_count_panel', 'ACTIVE', current_timestamp, 'System', 'CacheMiss');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'integration_latency_panel', 'ACTIVE', current_timestamp, 'System', 'IntegrationLatency');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'latency_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'uptime_of_deployment_stages', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'response_time_panel', 'ACTIVE', current_timestamp, 'System', 'Response Time');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'downtime_incident_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', '4xx_errors_panel', 'ACTIVE', current_timestamp, 'System', '4XXError');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', '5xx_errors_panel', 'ACTIVE', current_timestamp, 'System', '5XXError');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'error_logs_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'top_events_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'successful_event_details_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'failed_event_details', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'number_of_calls_panel', 'ACTIVE', current_timestamp, 'System', 'Number Of Calls');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'invocation_trend_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'error_and_warning_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'throttling_trends_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'LAMBDA', 'error_messages_count_panel', 'ACTIVE', current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EC2', 'network_traffic_panel', 'ACTIVE', current_timestamp, 'System', 'Inbound Traffic,Outbound Traffic');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'EKS', 'total_api_calls_panel', 'ACTIVE', current_timestamp, 'System', 'AllocatableMemory');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'total_api_calls_panel', 'ACTIVE', current_timestamp, 'System', 'TotalApiCalls');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'transaction_logs_disk_usage_panel', 'ACTIVE', current_timestamp, 'System', 'Transaction_Logs_Disk_Usage');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'error_analysis_panel', 'ACTIVE', current_timestamp, 'System', 'AllocatableMemory');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'error_analysis_panel', 'ACTIVE', current_timestamp, 'System', 'AllocatableMemory');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'RDS', 'uptime_percentage_panel', 'ACTIVE', current_timestamp, 'System', 'AllocatableMemory');
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES(NULL, 'APIGATEWAY', NULL, NULL, current_timestamp, 'System', NULL);
INSERT INTO public.cloud_element_supported_api
(cloud, element_type, "name", status, created_on, created_by, frames)
VALUES('AWS', 'APIGATEWAY', 'uptime_percentage_panel', 'ACTIVE', current_timestamp, 'System', NULL);
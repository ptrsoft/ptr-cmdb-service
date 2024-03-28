package com.synectiks.asset.config;

/**
 * Application ReportingQueryConstants.
 */
public final class ReportingQueryConstants {

    public static String SPEND_OVERVIEW="with s as (\t\n" +
            "WITH ServiceCosts AS (\n" +
            "   SELECT\n" +
            "       ce.service_category,\n" +
            "       SUM(CAST(jb.value AS int)) AS total,\n" +
            "       null as percentage\n" +
            "   FROM\n" +
            "       cloud_element ce,\n" +
            "       landingzone l,\n" +
            "       department d,\n" +
            "       organization o,\n" +
            "        jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)\n" +
            "   WHERE\n" +
            "       l.department_id = d.id\n" +
            "       AND d.organization_id = o.id\n" +
            "       AND ce.landingzone_id = l.id\n" +
            "       AND jb.key >= ? AND jb.key <= ? \n" +
            "       and upper(l.cloud) = upper(?)\n" +
            "       AND o.id = ? \n" +
            "       #DYNAMIC_CONDITION# \n" +
            "   GROUP BY\n" +
            "       ce.service_category\n" +
            ")\n" +
            "SELECT service_category, total, percentage FROM ServiceCosts),\n" +
            "ps as ( with psum as (select sum(total) as sumall from s)\tselect sumall from psum), \n" +
            "res as (select  s.service_category, s.total, ROUND((s.total * 100.0 / ps.sumall), 2) AS percentage from s, ps\n" +
            "union all\n" +
            "select  'Grand Total' as service_category, ps.sumall as total, null as percentage from ps)\n" +
            "select ROW_NUMBER() OVER () as id, service_category, total, percentage from res\n";

    public static String TOP_USED_SERVICES="with p as (   \n" +
            "\t  with PreviousServiceCosts as (   \n" +
            "\t  select   \n" +
            "\t   ce.element_type,   \n" +
            "\t   SUM(cast(jb.value as int)) as total   \n" +
            "\t  from   \n" +
            "\t   cloud_element ce,   \n" +
            "\t   landingzone l,   \n" +
            "\t   department d,   \n" +
            "\t   organization o,   \n" +
            "\t   jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key,   \n" +
            "\t   value)   \n" +
            "\t  where   \n" +
            "\t   l.department_id = d.id   \n" +
            "\t   and d.organization_id = o.id   \n" +
            "\t   and ce.landingzone_id = l.id   \n" +
            "\t   and jb.key >= ?    \n" +
            "\t   and jb.key <= ?    \n" +
            "\t   and o.id = ?    \n" +
            "\t   and upper(l.cloud) = upper(?)   \n" +
            "\t    #DYNAMIC_CONDITION#    \n" +
            "\t  group by    \n" +
            "\t   ce.element_type)   \n" +
            "\t  select   \n" +
            "\t    element_type,   \n" +
            "\t   SUM(total) as total   \n" +
            "\t  from    \n" +
            "\t   PreviousServiceCosts group by element_type),    \n" +
            "  ct as ( with CurrentServiceCosts as (   \n" +
            "\t  select   \n" +
            "\t   ce.element_type,   \n" +
            "\t   SUM(cast(jb.value as int)) as total   \n" +
            "\t  from   \n" +
            "\t   cloud_element ce,   \n" +
            "\t   landingzone l,   \n" +
            "\t   department d,   \n" +
            "\t   organization o,   \n" +
            "\t   jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key,   \n" +
            "\t   value)   \n" +
            "\t  where   \n" +
            "\t   l.department_id = d.id   \n" +
            "\t   and d.organization_id = o.id   \n" +
            "\t   and ce.landingzone_id = l.id   \n" +
            "\t   and jb.key >= ?    \n" +
            "\t   and jb.key <= ?    \n" +
            "\t   and o.id = ?    \n" +
            "\t   and upper(l.cloud) = upper(?)   \n" +
            "\t    #DYNAMIC_CONDITION#    \n" +
            "\t  group by   \n" +
            "\t   ce.element_type)   \n" +
            "\t  select   \n" +
            "\t   element_type,   \n" +
            "\t   SUM(total) as total   \n" +
            "\t  from   \n" +
            "\t   CurrentServiceCosts group by element_type),   \n" +
            "  ct_list as ( with CurrentServiceCosts as (   \n" +
            "\t  select   \n" +
            "\t   ce.element_type,   \n" +
            "\t   SUM(cast(jb.value as int)) as total   \n" +
            "\t  from   \n" +
            "\t   cloud_element ce,   \n" +
            "\t   landingzone l,   \n" +
            "\t   department d,   \n" +
            "\t   organization o,   \n" +
            "\t   jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key,   \n" +
            "\t   value)   \n" +
            "\t  where   \n" +
            "\t   l.department_id = d.id   \n" +
            "\t   and d.organization_id = o.id   \n" +
            "\t   and ce.landingzone_id = l.id   \n" +
            "\t   and jb.key >= ?    \n" +
            "\t   and jb.key <= ?    \n" +
            "\t   and o.id = ?    \n" +
            "\t   and upper(l.cloud) = upper(?)    \n" +
            "\t    #DYNAMIC_CONDITION#    \n" +
            "\t  group by   \n" +
            "\t   ce.element_type)   \n" +
            "\t  select   \n" +
            "\t   element_type,   \n" +
            "\t   total   \n" +
            "\t  from   \n" +
            "\t   CurrentServiceCosts   \n" +
            "\t  order by total #DYNAMIC_ORDER# #DYNAMIC_LIMIT# \n" +
            "\t  ),    \n" +
            "\t  f as (select upper('Previous_Total') as element_type, sum(p.total) as total from p group by element_type   \n" +
            "\t union all   \n" +
            "\t  select upper('Current_Total') as element_type, sum(ct.total) as total from ct group by element_type    \n" +
            "\t union all    \n" +
            "\t  select upper('Percentage'), round(((ct.total-p.total)/ ct.total)* 100, 2) from ct left join p on ct.element_type = p.element_type  \n" +
            "\t union all   \n" +
            "\t  select upper(ctl.element_type), ctl.total from ct_list ctl)    \n" +
            "\t  select ROW_NUMBER() OVER () as id, element_type, sum(total) as total from f group by element_type ";

    public static String COST_OF_TOP_ACCOUNTS= "  with budget_temp as (\n" +
            "  select\n" +
            "   b.id,\n" +
            "   b.allocated_budget,\n" +
            "   b.organization_id,\n" +
            "   b.status,\n" +
            "   b.financial_year_start,\n" +
            "   b.financial_year_end , \n" +
            "   cast(bg.obj -> 'depId' as int) as dep_id,\n" +
            "   cast(bg.obj -> 'allocatedBudget' as int) as dep_allocated_budget\n" +
            "  from\n" +
            "   organization o,\n" +
            "   budget b,\n" +
            "   jsonb_array_elements(b.budget_json -> 'data') with ordinality bg(obj, pos)\n" +
            "  where\n" +
            "   o.id = b.organization_id \n" +
            "   and b.financial_year_start >= ? \n" +
            "   and b.financial_year_end <= ? \n" +
            "   and o.id = ? ),\n" +
            " ce_temp as (\n" +
            "  select\n" +
            "   o.id as organization_id,\n" +
            "   d.id as department_id,\n" +
            "   d.\"name\" as department,\n" +
            "   SUM(cast(jb.value as int)) as total\n" +
            "  from\n" +
            "   cloud_element ce, \n" +
            "      landingzone l,\n" +
            "      department d,\n" +
            "      organization o,\n" +
            "      jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key, value)\n" +
            "  where\n" +
            "   l.department_id = d.id\n" +
            "   and d.organization_id = o.id\n" +
            "   and ce.landingzone_id = l.id\n" +
            "   and jb.key >= ? \n" +
            "   and jb.key <= ? \n" +
            "   and upper(l.cloud) = upper(?)\n" +
            "   #DYNAMIC_CONDITION# \n" +
            "   and o.id = ? \n" +
            "  group by o.id, d.id, d.\"name\"\n" +
            "  order by total desc\n" +
            ")\n" +
            " select\n" +
            "  distinct ct.department_id as id,\n" +
            "  ct.department,\n" +
            "  ct.total,\n" +
            "  bt.dep_allocated_budget,\n" +
            "  (bt.dep_allocated_budget - ct.total) as budget_consumed\n" +
            " from\n" +
            "  ce_temp ct\n" +
            " left join budget_temp bt \n" +
            "  on\n" +
            "  ct.organization_id = bt.organization_id and ct.department_id = bt.dep_id \n" +
            " order by \n" +
            "  ct.total #DYNAMIC_ORDER# #DYNAMIC_LIMIT# \n ";

    public static String SPENDING_TREND = "with p as (\n" +
            "  with PreviousServiceCosts as (\n" +
            " select\n" +
            "  'previous' as tenure,\n" +
            "  jb.key as dates,\n" +
            "  SUM(cast(jb.value as int)) as total\n" +
            " from\n" +
            "  cloud_element ce,\n" +
            "  landingzone l,\n" +
            "  department d,\n" +
            "  organization o,\n" +
            "  jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key, value)\n" +
            " where\n" +
            "  l.department_id = d.id\n" +
            "  and d.organization_id = o.id\n" +
            "  and ce.landingzone_id = l.id\n" +
            "  and jb.key >= ?\n" +
            "  and jb.key <= ?\n" +
            "  and o.id = ?\n" +
            " group by \n" +
            "  jb.key)\n" +
            " select\n" +
            "  tenure,\n" +
            "  dates,\n" +
            "  total\n" +
            " from \n" +
            "  PreviousServiceCosts), \n" +
            " ct as ( with CurrentServiceCosts as (\n" +
            " select\n" +
            "  'current' as tenure,\n" +
            "  jb.key as dates,\n" +
            "  SUM(cast(jb.value as int)) as total\n" +
            " from\n" +
            "  cloud_element ce,\n" +
            "  landingzone l,\n" +
            "  department d,\n" +
            "  organization o,\n" +
            "  jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key, value)\n" +
            " where\n" +
            "  l.department_id = d.id\n" +
            "  and d.organization_id = o.id\n" +
            "  and ce.landingzone_id = l.id\n" +
            "  and jb.key >= ?\n" +
            "  and jb.key <= ?\n" +
            "  and o.id = ?\n" +
            " group by\n" +
            "  jb.key)\n" +
            " select\n" +
            "  tenure,\n" +
            "  dates,\n" +
            "  total\n" +
            " from\n" +
            "  CurrentServiceCosts),\n" +
            " f as ( with FutureServiceCosts as (\n" +
            "  SELECT 'forcast' as tenure, cast(cast (generate_series(cast(? as date), cast(? as date), '1 day') as date) as text) AS dates, cast (floor(random() * 10000 + 1) as int) as total \n" +
            "  )\n" +
            " select\n" +
            "  tenure,\n" +
            "  dates,\n" +
            "  total\n" +
            " from\n" +
            "  FutureServiceCosts),\n" +
            " pmin as ( with pminimum as (select 'min' as tenure, null as dates, min(total) as total from p) select tenure, dates, total from pminimum),\n" +
            " ctmin as ( with ctminimum as (select 'min' as tenure, null as dates, min(total) as total from ct) select tenure, dates, total from ctminimum),\n" +
            " fmin as ( with fminimum as (select 'min' as tenure, null as dates, min(total) as total from f) select tenure, dates, total from fminimum),\n" +
            " allmin as (with allminimum as (\n" +
            "  select tenure, dates, total from pmin\n" +
            "  union all \n" +
            "  select tenure, dates, total from ctmin\n" +
            "  union all\n" +
            "  select tenure, dates, total from fmin\n" +
            " ) select 'min', null as dates, min(total) as total from allminimum),\n" +
            " \n" +
            " pmax as ( with pmaximum as (select 'max' as tenure, null as dates, max(total) as total from p) select tenure, dates, total from pmaximum),\n" +
            " ctmax as ( with ctmaximum as (select 'max' as tenure, null as dates, max(total) as total from ct) select tenure, dates, total from ctmaximum),\n" +
            " fmax as ( with fmaximum as (select 'max' as tenure, null as dates, max(total) as total from f) select tenure, dates, total from fmaximum),\n" +
            " allmax as (with allmaximum as (\n" +
            "  select tenure, dates, total from pmax\n" +
            "  union all \n" +
            "  select tenure, dates, total from ctmax\n" +
            "  union all\n" +
            "  select tenure, dates, total from fmax\n" +
            " ) select 'max', null as dates, max(total) as total from allmaximum),\n" +
            " res as (select 'max' as tenure, null as dates, total from allmax\n" +
            "  union all\n" +
            "  select 'min' as tenure, null as dates, total from allmin\n" +
            "  union all\n" +
            "  select  tenure, dates,  total from p \n" +
            "  union all\n" +
            "  select  tenure, dates,  total from ct \n" +
            "  union all\n" +
            "  select  tenure, dates,  total from f )\n" +
            " select ROW_NUMBER() OVER () as id, tenure, dates, total from res order by id asc";


    public static String SPEND_OVERVIEW_DETAIL = " with ltl as ( \n" +
            "\t with last_to_last as  \n" +
            "\t (select distinct ce.element_type, SUM(CAST(jb.value AS int)) AS total    \n" +
            "\t FROM  \n" +
            "\t  cloud_element ce,  \n" +
            "\t  landingzone l,  \n" +
            "\t  department d,  \n" +
            "\t  organization o,  \n" +
            "\t  jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)  \n" +
            "\t WHERE  \n" +
            "\t  l.department_id = d.id  \n" +
            "\t  AND d.organization_id = o.id  \n" +
            "\t  AND ce.landingzone_id = l.id  \n" +
            "\t  AND jb.key >= ? AND jb.key <= ?   \n" +
            "\t  and upper(l.cloud) = upper(?)  \n" +
            "\t  and upper(ce.service_category) = upper(?)   \n" +
            "\t  AND o.id = ? " +
            "       group by ce.element_type) select 'service_name' as service_name, sum(total) as total from last_to_last),   \n" +
            "\t prev_list as  \n" +
            "\t (select distinct ce.element_type, SUM(CAST(jb.value AS int)) AS total    \n" +
            "\t FROM  \n" +
            "\t  cloud_element ce,  \n" +
            "\t  landingzone l,  \n" +
            "\t  department d,  \n" +
            "\t  organization o,  \n" +
            "\t  jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)  \n" +
            "\t WHERE  \n" +
            "\t  l.department_id = d.id  \n" +
            "\t  AND d.organization_id = o.id  \n" +
            "\t  AND ce.landingzone_id = l.id  \n" +
            "\t  AND jb.key >= ? AND jb.key <= ?   \n" +
            "\t  and upper(l.cloud) = upper(?)  \n" +
            "\t  and upper(ce.service_category) = upper(?)   \n" +
            "\t  AND o.id = ? group by ce.element_type),  \n" +
            "\t curr_list as  \n" +
            "\t (select distinct ce.element_type, SUM(CAST(jb.value AS int)) AS total    \n" +
            "\t FROM  \n" +
            "\t  cloud_element ce,  \n" +
            "\t  landingzone l,  \n" +
            "\t  department d,  \n" +
            "\t  organization o,  \n" +
            "\t  jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)  \n" +
            "\t WHERE  \n" +
            "\t  l.department_id = d.id  \n" +
            "\t  AND d.organization_id = o.id  \n" +
            "\t  AND ce.landingzone_id = l.id  \n" +
            "\t  AND jb.key >= ? AND jb.key <= ?  \n" +
            "\t  and upper(l.cloud) = upper(?)  \n" +
            "\t  and upper(ce.service_category) = upper(?)   \n" +
            "\t  AND o.id = ? group by ce.element_type), \n" +
            "\t nof_current as ( with num_of_days_current as (SELECT (( EXTRACT(EPOCH FROM cast(? as date)) - EXTRACT(EPOCH FROM cast(? as date)) ) / 86400)+1 AS num_of_days) \n" +
            "\t  select num_of_days from num_of_days_current), \n" +
            "\t nof_previous as ( with num_of_days_current as (SELECT (( EXTRACT(EPOCH FROM cast(? as date)) - EXTRACT(EPOCH FROM cast(? as date)) ) / 86400)+1 AS num_of_days) \n" +
            "\t  select num_of_days from num_of_days_current),  \n" +
            "\t     prev_list_sum as (select 'service_name' as service_name, sum(total) as total from prev_list ), \n" +
            "\t     prev_avg_daily_spend as (select 'service_name' as service_name, cast(pls.total/(select num_of_days from nof_previous) as int) as average from prev_list_sum pls ), \n" +
            "\t curr_list_sum as (select 'service_name' as service_name, sum(total) as total from curr_list ), \n" +
            "\t future_cost as ( \n" +
            "\t  SELECT 'service_name' as service_name, (cls.total - cast (floor(random() * (80000 - 70000 + 1) + 70000) as int)) as total from curr_list_sum cls), \n" +
            "\t f as (select p.element_type as service_name, p.total as last_month_spend, c.total as this_month_spend, 0 as forecasted_spend, 0 as avg_daily_spend,  \n" +
            "\t round((c.total - p.total)/(c.total * 1.0 ) * 100 , 2) as variance   \n" +
            "\t from prev_list p left join curr_list c on p.element_type = c.element_type  \n" +
            "\t  union all  \n" +
            "\t select upper('total_last_mont_spend') as service_name, ps.total as last_month_spend, null as this_month_spend, 0 as forecasted_spend, 0 as avg_daily_spend,  \n" +
            "\t round((ps.total - ll.total)/(ps.total * 1.0 ) * 100 , 2) as variance  \n" +
            "\t from prev_list_sum ps left join ltl ll on ps.service_name = ll.service_name \n" +
            "\t  union all  \n" +
            "\t select upper('total_this_mont_spend') as service_name, null as last_month_spend, cls.total as this_month_spend, 0 as forecasted_spend, 0 as avg_daily_spend,  \n" +
            "\t round((cls.total - pls.total)/(cls.total * 1.0 ) * 100 , 2) as variance  \n" +
            "\t from curr_list_sum cls left join prev_list_sum pls on cls.service_name = pls.service_name \n" +
            "\t  union all \n" +
            "\t select upper('forecasted_spend') as service_name, null as last_month_spend, null as this_month_spend, fc.total as forecasted_spend, 0 as avg_daily_spend,  \n" +
            "\t round((fc.total - cls.total)/(fc.total * 1.0 ) * 100 , 2) as variance  \n" +
            "\t from future_cost fc left join curr_list_sum cls on fc.service_name = cls.service_name \n" +
            "\t  union all  \n" +
            "\t select upper('avg_daily_spend') as service_name, null as last_month_spend, null as this_month_spend, 0 as forecasted_spend,  \n" +
            "\t cast(cls.total/(select num_of_days from nof_current) as int) as avg_daily_spend,  \n" +
            "\t round((cast(cls.total/(select num_of_days from nof_current) as int) - cast(pls.total/(select num_of_days from nof_previous) as int))/(cast(cls.total/(select num_of_days from nof_current) as int) * 1.0 ) * 100 , 2) as variance  \n" +
            "\t from curr_list_sum cls left join prev_list_sum pls on cls.service_name = pls.service_name) \n" +
            "\t select ROW_NUMBER() OVER () as id,service_name, last_month_spend, this_month_spend, forecasted_spend, avg_daily_spend, variance from f \n" +
            "\t order by id asc ";
    public static String TOP_USED_SERVICES_DETAIL = " with ltl as ( \n" +
            "\t with last_to_last as  \n" +
            "\t (select distinct ce.element_type, SUM(CAST(jb.value AS int)) AS total    \n" +
            "\t FROM  \n" +
            "\t  cloud_element ce,  \n" +
            "\t  landingzone l,  \n" +
            "\t  department d,  \n" +
            "\t  organization o,  \n" +
            "\t  jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)  \n" +
            "\t WHERE  \n" +
            "\t  l.department_id = d.id  \n" +
            "\t  AND d.organization_id = o.id  \n" +
            "\t  AND ce.landingzone_id = l.id  \n" +
            "\t  AND jb.key >= ? AND jb.key <= ?   \n" +
            "\t  and upper(l.cloud) = upper(?)  \n" +
            "\t  AND o.id = ? " +
            "       group by ce.element_type) select 'service_name' as service_name, sum(total) as total from last_to_last),   \n" +
            "\t prev_list as  \n" +
            "\t (select distinct ce.element_type, SUM(CAST(jb.value AS int)) AS total    \n" +
            "\t FROM  \n" +
            "\t  cloud_element ce,  \n" +
            "\t  landingzone l,  \n" +
            "\t  department d,  \n" +
            "\t  organization o,  \n" +
            "\t  jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)  \n" +
            "\t WHERE  \n" +
            "\t  l.department_id = d.id  \n" +
            "\t  AND d.organization_id = o.id  \n" +
            "\t  AND ce.landingzone_id = l.id  \n" +
            "\t  AND jb.key >= ? AND jb.key <= ?   \n" +
            "\t  and upper(l.cloud) = upper(?)  \n" +
            "\t  AND o.id = ? group by ce.element_type),  \n" +
            "\t curr_list as  \n" +
            "\t (select distinct ce.element_type, SUM(CAST(jb.value AS int)) AS total    \n" +
            "\t FROM  \n" +
            "\t  cloud_element ce,  \n" +
            "\t  landingzone l,  \n" +
            "\t  department d,  \n" +
            "\t  organization o,  \n" +
            "\t  jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)  \n" +
            "\t WHERE  \n" +
            "\t  l.department_id = d.id  \n" +
            "\t  AND d.organization_id = o.id  \n" +
            "\t  AND ce.landingzone_id = l.id  \n" +
            "\t  AND jb.key >= ? AND jb.key <= ?  \n" +
            "\t  and upper(l.cloud) = upper(?)  \n" +
            "\t  AND o.id = ? group by ce.element_type), \n" +
            "\t nof_current as ( with num_of_days_current as (SELECT (( EXTRACT(EPOCH FROM cast(? as date)) - EXTRACT(EPOCH FROM cast(? as date)) ) / 86400)+1 AS num_of_days) \n" +
            "\t  select num_of_days from num_of_days_current), \n" +
            "\t nof_previous as ( with num_of_days_current as (SELECT (( EXTRACT(EPOCH FROM cast(? as date)) - EXTRACT(EPOCH FROM cast(? as date)) ) / 86400)+1 AS num_of_days) \n" +
            "\t  select num_of_days from num_of_days_current),  \n" +
            "\t     prev_list_sum as (select 'service_name' as service_name, sum(total) as total from prev_list ), \n" +
            "\t     prev_avg_daily_spend as (select 'service_name' as service_name, cast(pls.total/(select num_of_days from nof_previous) as int) as average from prev_list_sum pls ), \n" +
            "\t curr_list_sum as (select 'service_name' as service_name, sum(total) as total from curr_list ), \n" +
            "\t future_cost as ( \n" +
            "\t  SELECT 'service_name' as service_name, (cls.total - cast (floor(random() * (80000 - 70000 + 1) + 70000) as int)) as total from curr_list_sum cls), \n" +
            "\t f as (select p.element_type as service_name, p.total as last_month_spend, c.total as this_month_spend, 0 as forecasted_spend, 0 as avg_daily_spend,  \n" +
            "\t round((c.total - p.total)/(c.total * 1.0 ) * 100 , 2) as variance   \n" +
            "\t from prev_list p left join curr_list c on p.element_type = c.element_type  \n" +
            "\t  union all  \n" +
            "\t select upper('total_last_mont_spend') as service_name, ps.total as last_month_spend, null as this_month_spend, 0 as forecasted_spend, 0 as avg_daily_spend,  \n" +
            "\t round((ps.total - ll.total)/(ps.total * 1.0 ) * 100 , 2) as variance  \n" +
            "\t from prev_list_sum ps left join ltl ll on ps.service_name = ll.service_name \n" +
            "\t  union all  \n" +
            "\t select upper('total_this_mont_spend') as service_name, null as last_month_spend, cls.total as this_month_spend, 0 as forecasted_spend, 0 as avg_daily_spend,  \n" +
            "\t round((cls.total - pls.total)/(cls.total * 1.0 ) * 100 , 2) as variance  \n" +
            "\t from curr_list_sum cls left join prev_list_sum pls on cls.service_name = pls.service_name \n" +
            "\t  union all \n" +
            "\t select upper('forecasted_spend') as service_name, null as last_month_spend, null as this_month_spend, fc.total as forecasted_spend, 0 as avg_daily_spend,  \n" +
            "\t round((fc.total - cls.total)/(fc.total * 1.0 ) * 100 , 2) as variance  \n" +
            "\t from future_cost fc left join curr_list_sum cls on fc.service_name = cls.service_name \n" +
            "\t  union all  \n" +
            "\t select upper('avg_daily_spend') as service_name, null as last_month_spend, null as this_month_spend, 0 as forecasted_spend,  \n" +
            "\t cast(cls.total/(select num_of_days from nof_current) as int) as avg_daily_spend,  \n" +
            "\t round((cast(cls.total/(select num_of_days from nof_current) as int) - cast(pls.total/(select num_of_days from nof_previous) as int))/(cast(cls.total/(select num_of_days from nof_current) as int) * 1.0 ) * 100 , 2) as variance  \n" +
            "\t from curr_list_sum cls left join prev_list_sum pls on cls.service_name = pls.service_name) \n" +
            "\t select ROW_NUMBER() OVER () as id,service_name, last_month_spend, this_month_spend, forecasted_spend, avg_daily_spend, variance from f \n" +
            "\t order by id asc ";

    public static String COST_OF_TOP_ACCOUNTS_DETAIL = "with budget_temp as (\n" +
            "   select\n" +
            "     distinct b.id,\n" +
            "     b.allocated_budget,\n" +
            "     b.organization_id,\n" +
            "     b.status,\n" +
            "     b.financial_year_start,\n" +
            "     b.financial_year_end , \n" +
            "     cast(bg.obj -> 'depId' as int) as dep_id,\n" +
            "     cast(bg.obj -> 'allocatedBudget' as int) as dep_allocated_budget\n" +
            "   from\n" +
            "     organization o,\n" +
            "     budget b,\n" +
            "     jsonb_array_elements(b.budget_json -> 'data') with ordinality bg(obj, pos)\n" +
            "   where\n" +
            "     o.id = b.organization_id \n" +
            "     and b.financial_year_start >= ? \n" +
            "     and b.financial_year_end <= ? \n" +
            "     and o.id = ? ),\n" +
            " \n" +
            " ltl as (\n" +
            "  with last_to_last as (\n" +
            "   select  \n" +
            "     o.id as org_id,\n" +
            "       l.landing_zone as account_id, \n" +
            "     d.\"name\" as department,\n" +
            "     pe.instance_id as vpc, \n" +
            "     SUM(cast(jb.value as int)) as total\n" +
            "     from\n" +
            "        cloud_element ce, \n" +
            "        landingzone l,\n" +
            "        department d,\n" +
            "        organization o,\n" +
            "        product_enclave pe, \n" +
            "        jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key, value)\n" +
            "     where\n" +
            "      l.department_id = d.id\n" +
            "      and d.organization_id = o.id\n" +
            "      and ce.landingzone_id = l.id\n" +
            "      and jb.key >= ?\n" +
            "      and jb.key <= ? \n" +
            "      and upper(l.cloud) = upper(?)\n" +
            "      and ce.product_enclave_id = pe.id \n" +
            "      and o.id = ? \n" +
            "     group by o.id, l.landing_zone, d.\"name\", pe.instance_id) \n" +
            "  select 'last_to_last' as last_to_last, sum(total) as total from last_to_last),\n" +
            "  \n" +
            " hosted_service as (\n" +
            "  with h as (\n" +
            "     select\n" +
            "       distinct l.landing_zone, c.obj -> 'serviceId' as service_id, ce.product_enclave_id\n" +
            "     from\n" +
            "        cloud_element ce, \n" +
            "        landingzone l,\n" +
            "        department d,\n" +
            "        organization o,\n" +
            "        jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key, value),\n" +
            "        jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos)\n" +
            "     where\n" +
            "      l.department_id = d.id\n" +
            "      and d.organization_id = o.id\n" +
            "      and ce.landingzone_id = l.id\n" +
            "      and jb.key >= ? \n" +
            "      and jb.key <= ? \n" +
            "      and upper(l.cloud) = upper(?)\n" +
            "      and ce.product_enclave_id is not null\n" +
            "      and o.id = ? \n" +
            "    ) select landing_zone, product_enclave_id, count(service_id) as total_services from h group by landing_zone, product_enclave_id\n" +
            "    ),\n" +
            "    prev_list as (\n" +
            "    with s as (\n" +
            "     select  \n" +
            "     o.id as org_id,\n" +
            "       l.landing_zone as account_id, \n" +
            "     d.\"name\" as department,\n" +
            "     pe.instance_id as vpc, \n" +
            "     SUM(cast(jb.value as int)) as total\n" +
            "     from\n" +
            "        cloud_element ce, \n" +
            "        landingzone l,\n" +
            "        department d,\n" +
            "        organization o,\n" +
            "        product_enclave pe, \n" +
            "        jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key, value)\n" +
            "     where\n" +
            "      l.department_id = d.id\n" +
            "      and d.organization_id = o.id\n" +
            "      and ce.landingzone_id = l.id\n" +
            "      and jb.key >= ? \n" +
            "      and jb.key <= ? \n" +
            "      and upper(l.cloud) = upper(?)\n" +
            "      and ce.product_enclave_id = pe.id \n" +
            "      and o.id = ? \n" +
            "     group by o.id, l.landing_zone, d.\"name\", pe.instance_id)  \n" +
            "   select org_id, account_id, department, vpc, total from s order by total #DYNAMIC_ORDER# #DYNAMIC_LIMIT# \n" +
            " ),\n" +
            "   curr_list as (\n" +
            "    with s as (\n" +
            "     select  \n" +
            "     o.id as org_id,\n" +
            "       l.landing_zone as account_id, \n" +
            "     d.\"name\" as department,\n" +
            "     pe.instance_id as vpc, \n" +
            "     SUM(cast(jb.value as int)) as total\n" +
            "     from\n" +
            "        cloud_element ce, \n" +
            "        landingzone l,\n" +
            "        department d,\n" +
            "        organization o,\n" +
            "        product_enclave pe, \n" +
            "        jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') as jb(key, value)\n" +
            "     where\n" +
            "      l.department_id = d.id\n" +
            "      and d.organization_id = o.id\n" +
            "      and ce.landingzone_id = l.id\n" +
            "      and jb.key >= ? \n" +
            "      and jb.key <= ? \n" +
            "      and upper(l.cloud) = upper(?)\n" +
            "      and ce.product_enclave_id = pe.id \n" +
            "      and o.id = ? \n" +
            "     group by o.id, l.landing_zone, d.\"name\", pe.instance_id)  \n" +
            "   select org_id, account_id, department, vpc, total from s order by total #DYNAMIC_ORDER# #DYNAMIC_LIMIT# \n" +
            " ),\n" +
            " prev_list_sum as (select sum(total) as total from prev_list ),\n" +
            "    curr_list_sum as (select sum(total) as total from curr_list ),\n" +
            "    nof_current as ( with num_of_days_current as (SELECT (( EXTRACT(EPOCH FROM cast(? as date)) - EXTRACT(EPOCH FROM cast(? as date)) ) / 86400)+1 AS num_of_days)\n" +
            "  select num_of_days from num_of_days_current),\n" +
            "  nof_previous as ( with num_of_days_current as (SELECT (( EXTRACT(EPOCH FROM cast(? as date)) - EXTRACT(EPOCH FROM cast(? as date)) ) / 86400)+1 AS num_of_days)\n" +
            "  select num_of_days from num_of_days_current),  \n" +
            "    future_cost as (SELECT 'service_name' as service_name, (cls.total - cast (floor(random() * (80000 - 70000 + 1) + 70000) as int)) as total from curr_list_sum cls),\n" +
            " f as (\n" +
            "  select cl.account_id, cl.department, cl.vpc, coalesce (hs.total_services, 0) as service_count, \n" +
            "  'US-East(N.Virginia)' as high_spending_region, cl.total as spending,\n" +
            "  round((cl.total - p.total)/(cl.total * 1.0 ) * 100 , 2) as variance, \n" +
            "  bt.dep_allocated_budget as budget\n" +
            "  from curr_list cl \n" +
            "   left join hosted_service hs on cl.account_id = hs.landing_zone \n" +
            "   left join budget_temp bt on cl.org_id = bt.organization_id\n" +
            "   left join prev_list p on cl.account_id = p.account_id and cl.org_id = p.org_id and cl.vpc = p.vpc\n" +
            "   union all \n" +
            "  select upper('last_month_spend') as account_id, null as department, null as vpc, null as service_count, null as high_spending_region,\n" +
            "  ps.total as spending, round((ps.total - ll.total)/(ps.total * 1.0 ) * 100 , 2) as variance, null as budget\n" +
            "  from prev_list_sum ps left join ltl ll on 1 = 1\n" +
            "   union all \n" +
            "  select upper('month_to_date_spend') as account_id, null as department, null as vpc, null as service_count, null as high_spending_region,\n" +
            "  cls.total as spending, round((cls.total - pls.total)/(cls.total * 1.0 ) * 100 , 2) as variance, null as budget\n" +
            "  from curr_list_sum cls left join prev_list_sum pls on 1 = 1 \n" +
            "   union all\n" +
            "  select upper('forecasted_spend') as account_id, null as department, null as vpc, null as service_count, null as high_spending_region,\n" +
            "  fc.total as spending, round((fc.total - cls.total)/(fc.total * 1.0 ) * 100 , 2) as variance, null as budget \n" +
            "  from future_cost fc left join curr_list_sum cls on 1 = 1\n" +
            "   union all\n" +
            "  select upper('avg_daily_spend') as account_id, null as department, null as vpc, null as service_count, null as high_spending_region,\n" +
            "  cast(cls.total/(select num_of_days from nof_current) as int) as spending, \n" +
            "  round((cast(cls.total/(select num_of_days from nof_current) as int) - cast(pls.total/(select num_of_days from nof_previous) as int))/(cast(cls.total/(select num_of_days from nof_current) as int) * 1.0 ) * 100 , 2) as variance, \n" +
            "  null as budget \n" +
            "  from curr_list_sum cls left join prev_list_sum pls on 1 = 1)\n" +
            " select ROW_NUMBER() OVER () as id,account_id, department, vpc, service_count, high_spending_region, spending, variance, budget from f\n" +
            "  ";

    public static String POTENTIAL_SAVINGS_DETAIL_TOP_RI_RECOMMENDATION = "select ce.id, ce.element_type, ce.instance_id, 'RI' as recommendation, 't4g.2xlarge' as current_instance, \n" +
            "\t't2.2xlarge' as recommended_instance, '1yr RI' as terms, 'No Upfront' as payment_mode, '0' as upfront_cost,\n" +
            "\tround(cast (random() as numeric)  * (0.9 - 0.1 + 1) + 0.1 , 2) as per_hour_cost,\n" +
            "\tcast (random() as int)  * (800 - 250 + 1) + 250  as estimated_savings,\n" +
            "\tSUM(CAST(jb.value AS int)) AS total_spend   \n" +
            "\tFROM \n" +
            "\t\tcloud_element ce, \n" +
            "\t\tlandingzone l, \n" +
            "\t\tdepartment d, \n" +
            "\t\torganization o, \n" +
            "\t\tjsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value) \n" +
            "\tWHERE \n" +
            "\t\tl.department_id = d.id \n" +
            "\t\tAND d.organization_id = o.id \n" +
            "\t\tAND ce.landingzone_id = l.id \n" +
            "\t\tAND jb.key >= ? AND jb.key <= ?  \n" +
            "\t\tand upper(l.cloud) = upper(?) \n" +
            "\t\tand upper(ce.service_category) = upper(?)  \n" +
            "\t\tAND o.id = ? group by ce.id, ce.element_type, ce.instance_id,ce.config_json ";

    public static String SPEND_OVERVIEW_ELEMENT_DETAIL ="select  ce.id, ce.element_type, ce.instance_id, 'dev-prod' as tags, 't2.2xlarge' as instance_type, 'Running' as instance_status, 'on Demand' as pricing_model, \n" +
            "\t'us-east-1a' as availability_zone, '0.0015' as ondemand_cost_per_hr, 'Unavailable' as ri_cost_per_hr,\n" +
            "\t'720hrs' as usage_hours, 'NA' as add_ons, SUM(CAST(jb.value AS int)) AS total_spend   \n" +
            "\tFROM \n" +
            "\t\tcloud_element ce, \n" +
            "\t\tlandingzone l, \n" +
            "\t\tdepartment d, \n" +
            "\t\torganization o, \n" +
            "\t\tjsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value) \n" +
            "\tWHERE \n" +
            "\t\tl.department_id = d.id \n" +
            "\t\tAND d.organization_id = o.id \n" +
            "\t\tAND ce.landingzone_id = l.id \n" +
            "\t\tAND jb.key >= ? AND jb.key <= ?  \n" +
            "\t\tand upper(l.cloud) = upper(?) \n" +
            "\t\tand upper(ce.service_category) = upper(?) \n" +
            "\t\tand upper(ce.element_type) = upper(?)\n" +
            "\t\tAND o.id = ? \n" +
            "\t\tgroup by ce.id, ce.element_type,ce.instance_id, ce.config_json";

    public static String SPEND_OVERVIEW_ELEMENT_SUMMARY ="with cur as (\n" +
            "\t\tselect ce.element_type, count(ce.element_type) as total_instance\t\n" +
            "\t\tFROM \n" +
            "\t\t\tcloud_element ce, \n" +
            "\t\t\tlandingzone l, \n" +
            "\t\t\tdepartment d, \n" +
            "\t\t\torganization o, \n" +
            "\t\t\tjsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value) \n" +
            "\t\tWHERE \n" +
            "\t\t\tl.department_id = d.id \n" +
            "\t\t\tAND d.organization_id = o.id \n" +
            "\t\t\tAND ce.landingzone_id = l.id \n" +
            "\t\t\tAND jb.key >= ? AND jb.key <= ?  \n" +
            "\t\t\tand upper(l.cloud) = upper(?) \n" +
            "\t\t\tand upper(ce.service_category) = upper(?) \n" +
            "\t\t\tand upper(ce.element_type) = upper(?)\n" +
            "\t\t\tAND o.id = ? \n" +
            "\t\t\tgroup by ce.element_type),\n" +
            "\tprev as (\n" +
            "\t\tselect ce.element_type, count(ce.element_type) as total_instance\t\n" +
            "\t\tFROM \n" +
            "\t\t\tcloud_element ce, \n" +
            "\t\t\tlandingzone l, \n" +
            "\t\t\tdepartment d, \n" +
            "\t\t\torganization o, \n" +
            "\t\t\tjsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value) \n" +
            "\t\tWHERE \n" +
            "\t\t\tl.department_id = d.id \n" +
            "\t\t\tAND d.organization_id = o.id \n" +
            "\t\t\tAND ce.landingzone_id = l.id \n" +
            "\t\t\tAND jb.key >= ? AND jb.key <= ?  \n" +
            "\t\t\tand upper(l.cloud) = upper(?) \n" +
            "\t\t\tand upper(ce.service_category) = upper(?) \n" +
            "\t\t\tand upper(ce.element_type) = upper(?)\n" +
            "\t\t\tAND o.id = ? \n" +
            "\t\t\tgroup by ce.element_type),\n" +
            "\ttotal_summary as (\n" +
            "\t\tselect 'total instances' as instance_desc, c.total_instance as current_total, p.total_instance as previous_total, \n" +
            "\t\tround((c.total_instance - p.total_instance)/(c.total_instance * 1.0 ) * 100 , 2) as variance   \n" +
            "\t\tfrom cur c left join prev p on c.element_type = p.element_type),\n" +
            "\trunning as (select 'running' instance_desc, cast (floor(random() * (80000 - 70000 + 1) + 70000) as int) as current_total, \n" +
            "\t\t\tcast (floor(random() * (90000 - 60000 + 1) + 60000) as int) as previous_total, \n" +
            "\t\t\t0 as variance from total_summary),\n" +
            "\tstopped as (select 'stopped' instance_desc, cast (floor(random() * (90000 - 90000 + 1) + 80000) as int) as current_total, \n" +
            "\t\t\tcast (floor(random() * (90000 - 60000 + 1) + 60000) as int) as previous_total, \n" +
            "\t\t\t0 as variance from total_summary),\n" +
            "\tterminated as (select 'terminated' instance_desc, cast (floor(random() * (90000 - 90000 + 1) + 80000) as int) as current_total, \n" +
            "\t\t\tcast (floor(random() * (90000 - 60000 + 1) + 60000) as int) as previous_total, \n" +
            "\t\t\t0 as variance from total_summary),\t\t\n" +
            "\tf as (select instance_desc, current_total, previous_total, variance from total_summary\n" +
            "\t\t\tunion all\n" +
            "\t\t\tselect instance_desc, current_total, previous_total, round((current_total - previous_total)/(current_total * 1.0 ) * 100 , 2) as variance from running\n" +
            "\t\t\tunion all\n" +
            "\t\t\tselect instance_desc, current_total, previous_total, round((current_total - previous_total)/(current_total * 1.0 ) * 100 , 2) as variance from stopped\n" +
            "\t\t\tunion all\n" +
            "\t\t\tselect instance_desc, current_total, previous_total, round((current_total - previous_total)/(current_total * 1.0 ) * 100 , 2) as variance from terminated)\n" +
            "\tselect  ROW_NUMBER() OVER () as id, instance_desc, current_total, previous_total, variance from f";
    private ReportingQueryConstants() {
    }
}

#!/usr/bin/env bash
# jcmd|awk '{print $1}'|xargs kill -9
agent_path=your_agent_path
scad_path=your_project_path

service_name_prefix=-Dskywalking.agent.service_name=scad::
instance_name_param=-Dskywalking.agent.instance_name=scad
backend_service_param=-Dskywalking.collector.backend_service=localhost:11800
extra_param="-Dskywalking.trace.ignore_path=/actuator/**,/,/eureka/** -Dskywalking.plugin.jdbc.trace_sql_parameters=true -Dskywalking.plugin.springmvc.collect_http_params=true -Dskywalking.plugin.httpclient.collect_http_params=true"
javaagent_param=-javaagent:$agent_path/skywalking-agent.jar
common_param="$instance_name_param $backend_service_param $extra_param $javaagent_param"

echo start usp
nohup java ${service_name_prefix}scad-usp $common_param -jar $scad_path/user-service-provider/target/user-service-provider.jar --spring.profiles.active=test >/dev/null 2>&1 &
echo start usi
nohup java ${service_name_prefix}scad-usi $common_param -jar $scad_path/user-service-invoker/target/user-service-invoker.jar --spring.profiles.active=test >/dev/null 2>&1 &
echo start order-service
nohup java ${service_name_prefix}scad-order-service $common_param -jar $scad_path/order-service/target/order-service.jar >/dev/null 2>&1 &
echo start gateway
nohup java ${service_name_prefix}scad-gateway $common_param -jar $scad_path/spring-cloud-gateway/target/spring-cloud-gateway.jar >/dev/null 2>&1 &
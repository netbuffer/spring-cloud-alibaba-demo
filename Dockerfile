FROM javawiki/supervisord:v4.1.0-alijdk21
ARG project_dir=/usr/local/project/
COPY order-service/target/*.jar $project_dir
COPY spring-cloud-gateway/target/*.jar $project_dir
COPY user-service-invoker/target/*.jar $project_dir
COPY user-service-provider/target/*.jar $project_dir
COPY spring-cloud-alibaba-demo.ini /etc/supervisor/conf.d/spring-cloud-alibaba-demo.conf

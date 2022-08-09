FROM javawiki/supervisord:v4.0.2-openjdk8
ARG project_dir=/usr/local/project/
COPY order-service/target/*.jar $project_dir
COPY spring-cloud-gateway/target/*.jar $project_dir
COPY user-service-invoker/target/*.jar $project_dir
COPY user-service-provider/target/*.jar $project_dir
COPY spring-cloud-alibaba-demo.ini /etc/supervisor.d/spring-cloud-alibaba-demo.conf
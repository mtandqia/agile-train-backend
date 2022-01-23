FROM openjdk:8-jdk-alpine
MAINTAINER lmt <lemonlepidii@gmail.com>
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
RUN apk add mysql mysql-client
RUN ip -4 route list match 0/0 | awk '{print $3 " host.docker.internal"}' >> /etc/hosts
ENTRYPOINT ["java", "-Dspring.profiles.active=prod,redisdocker", "-jar", "/app.jar"]
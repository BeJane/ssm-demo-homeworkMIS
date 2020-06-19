FROM openjdk:8
MAINTAINER wujingqi
RUN echo "Asia/Shanghai" > /etc/timezone
VOLUME /tmp
ADD 17301169-Homework.jar /17301169-Homework.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/17301169-Homework.jar"]
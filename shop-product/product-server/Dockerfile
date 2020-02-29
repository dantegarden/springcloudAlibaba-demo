FROM hub.c.163.com/library/java:8-alpine
#VOLUME /home/lij/docker/tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
FROM openjdk:11-jre-slim-stretch
VOLUME /tmp
COPY build/libs/wirelog-all.jar app.jar
EXPOSE 5050
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
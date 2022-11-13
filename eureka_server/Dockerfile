FROM adoptopenjdk/openjdk11:alpine-jre
RUN apk --no-cache add curl
VOLUME /tmp
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

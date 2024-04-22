FROM gradle:8.7.0-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:21
EXPOSE 80:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/callme-auth.jar
ENTRYPOINT ["java","-jar","/app/callme-auth.jar"]
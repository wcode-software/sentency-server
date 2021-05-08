FROM openjdk:14-alpine AS build
COPY . /sentency-server
WORKDIR /sentency-server
RUN ./gradlew assemble

FROM openjdk:14-alpine
EXPOSE 7000
RUN mkdir /app
COPY --from=build /sentency-server/build/libs/*.jar /app/sentency-server.jar
RUN mkdir /resources
COPY --from=build /sentency-server/resources/*.env /resources/
ENTRYPOINT ["java","-jar","/app/sentency-server.jar"]

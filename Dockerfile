FROM openjdk:14-alpine AS build
COPY . /sentency-server
WORKDIR /sentency-server
RUN ./gradlew installDist

FROM openjdk:14-alpine
EXPOSE 7000:7000
RUN mkdir /app
COPY --from=build /sentency-server/build/install/sentency-server/ /app/
WORKDIR /app/bin
CMD ["./sentency-server"]

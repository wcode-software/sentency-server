FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 7000:7000
RUN mkdir /app
COPY  ./build/install/sentency-server/ /app/
WORKDIR /app/bin
CMD ["./sentency-server"]

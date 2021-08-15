FROM ghcr.io/graalvm/graalvm-ce:ol7-java11-21.2.0 as build
WORKDIR /author-service
COPY . /author-service
RUN yum install -y -q xz
RUN curl -sL -o - https://github.com/upx/upx/releases/download/v3.96/upx-3.96-amd64_linux.tar.xz | tar xJ
RUN gu install native-image
RUN ./gradlew assemble
RUN native-image --no-fallback --static -jar /author-service/build/libs/author-service-*-all.jar service
RUN ./upx-3.96-amd64_linux/upx -7 /author-service/service

FROM scratch
EXPOSE 7000:7000
COPY --from=build /author-service/service /service
CMD ["./service"]

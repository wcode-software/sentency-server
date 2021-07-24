## Micronaut 2.5.9 Documentation

- [User Guide](https://docs.micronaut.io/2.5.9/guide/index.html)
- [API Reference](https://docs.micronaut.io/2.5.9/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/2.5.9/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

## Bulding and Running

The current service is deployed making use of GraalVM and UPX to create a compressed native image.

Before executing all the commands is necessary to install both [GraalVM](https://www.graalvm.org/)
and [UPX](https://upx.github.io/) on your machine.

Use the following command to generate the native image:

```bash
./gradlew nativeImage 
```

After the build compress the image using UPX:

```bash
upx -7 build/native-image/author-service
```

To run the compressed image just run the command:

```bash
./build/native-image/author-service
```

## Docker image

Dockerfile is already configured to build a docker image using GraalVM and UPX to make the smaller possible
version. To create the image run the command:

```bash
docker build -t author-service . 
```

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=walterjgsp_sentency-server&metric=alert_status)](https://sonarcloud.io/dashboard?id=walterjgsp_sentency-server)

<div align="center">
    <h1 align="center">Sentency Server</h1>
    <h5>Project Sentency</h5>
    <img  width="80" height="80" src="./icon.svg">
</div>

Repository to hold Sentency server code. The server is be done using Ktor, a simple web framework for Kotlin.

## Third-Party Libraries

Ktor come with built-in libraries but it's mostly a "plug what you need" type of framework, because of that we had to 
add some third party libraries to fill some gaps:

* [Ktor](https://ktor.io/): Ktor is an asynchronous framework for creating microservices, web applications, and more.
* [Exposed](https://github.com/JetBrains/Exposed): an ORM framework for Kotlin created by JetBrains
* [KMongo](https://litote.org/kmongo/): a Kotlin toolkit for Mongo
* [Koin](https://insert-koin.io/): a smart Kotlin injection library to keep you focused on your app, not on your tools

## Docker

The project is configured to be deployed using docker.
First step is to create the distribution of the application (in this case using Gradle):

```bash
./gradlew installDist
```

To build the docker image go to the root of the project and run the command:

```bash
 docker build -t sentency-server .
```

After the image is build to run locally you can execute:

```bash
docker run -p 7000:7000 sentency-server:latest 
```

## Environment variables

The project load environment variables to fill some important parameters on the server.
They can be defined when running the docker image, on the docker-compose or in the environment
itself.

* FLAVOR: If it is production or development. Production flavor will try to use Postgre
* API_KEY: Value that will be used to wrap all calls.
* DB_NAME: When in production the name of the database
* DB_USERNAME: When in production the username of the user that connects to database
* DB_PASSWORD: When in production the password to connect to database
* JWT_SECRET: Secret used to generate JWT tokens
* JWT_ISSUER: Issuer of the JWT tokens
* MONGO_HOST: Host address of Mongo Database
* RECAPTCHA_SECRET: ReCaptcha secret key to validate ReCaptcha V3 tokens

## Test coverage

To generate test coverage report Jacoco was configured as an extension of the test task. Execute
the command:

```bash
./gradlew test
```

to execute tests and generate code coverage reports.

# Sentency Server

The server will be done using Javalin, a simple web framework for Java and Kotlin.

## Third-Party Libraries

Javalin come with built-in libraries but it's mostly a "plug what you need" type of framework, because of that we had to 
add some third party libraries to fill some gaps:

* [Ktor](https://ktor.io/): Ktor is an asynchronous framework for creating microservices, web applications, and more.
* [Exposed](https://github.com/JetBrains/Exposed): an ORM framework for Kotlin created by JetBrains

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
docker run -p 5000:7000 sentency-server:latest 
```

Open the address **http://localhost:5000/docs** on your browser and you will see this project Swagger with
APIs.

## Environment variables

The project use .env libraries to load environment variables. To change any parameter change the value inside 
the file **development.env** that's inside the resource folder.

If it's necessary to create a new .env file (production file for example), the file should comply with the following
pattern:

```bash
ENVIRONMENT = development
PREFIX = server
```

Inside the **Application.kt** the *loadEnvVariables()* function need to be update to match the new file 
name and folder parameters

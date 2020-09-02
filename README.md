# Sentency Server

The server will be done using Javalin, a simple web framework for Java and Kotlin.

## Third-Party Libraries

Javalin come with built-in libraries but it's mostly a "plug what you need" type of framework, because of that we had to 
add some third party libraries to fill some gaps:

* [Javalin](https://javalin.io/): a simple web framework for Java and Kotlin
* [Exposed](https://github.com/JetBrains/Exposed): an ORM framework for Kotlin created by JetBrains

## Docker

The project is configured to be deployed using docker. To build the docker image go to the root of the project and run
the command:

```bash
 docker build -t sentency-server .
```

After the image is build to run locally you can execute:

```bash
docker run -p 5000:7000 sentency-server:latest 
```

Open the address **http://localhost:5000/docs** on your browser and you will see this project Swagger with
APIs.
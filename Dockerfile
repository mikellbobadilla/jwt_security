FROM alpine:3.17.3
WORKDIR /app
RUN apk add openjdk17 && apk add maven
COPY . /app
RUN mvn clean
RUN mvn install '-Dmaven.test.skip=true'
ENTRYPOINT ["java", "-jar", "/app/target/jwt_security-1.0.0.jar"]
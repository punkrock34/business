# React build
FROM node:18.2 as react-build
WORKDIR /app
COPY package.json webpack.config.js /app/
COPY src/main/resources/static /app/src/main/resources/static
RUN npm install
RUN npx webpack

# Spring Boot build
FROM maven:3.8.4-openjdk-17 as spring-build
WORKDIR /app
COPY pom.xml mvnw mvnw.cmd .mvn .vscode /app/
COPY src /app/src
RUN rm -rf /app/src/main/resources/static/bundles
COPY --from=react-build /app/src/main/resources/static/bundles /app/src/main/resources/static/bundles
RUN mvn clean install

# final image
FROM openjdk:17
WORKDIR /app
COPY --from=spring-build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

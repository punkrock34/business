# Stage 1: Build the React frontend
FROM node:18.2 as react-build
WORKDIR /app
COPY package.json webpack.config.js /app/
COPY src/main/resources/static /app/src/main/resources/static
RUN npm install
RUN npx webpack

# Stage 2: Build the Spring Boot application
FROM maven:3.8.4-openjdk-17 as spring-build
WORKDIR /app
COPY pom.xml mvnw mvnw.cmd .mvn /app/
COPY src /app/src
COPY --from=react-build /app/src/main/resources/static/bundles /app/src/main/resources/static/bundles
RUN mvn clean install

# Stage 3: Create the final image
FROM openjdk:22-ea-10-jdk-slim
WORKDIR /app
COPY --from=spring-build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

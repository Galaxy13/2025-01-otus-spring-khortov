FROM gradle:8.8.0-jdk21-alpine AS build
RUN apk add --no-cache nodejs npm
WORKDIR /app
COPY . .
RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S app -G appgroup
USER app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
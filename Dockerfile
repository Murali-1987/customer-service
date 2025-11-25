# -------- build stage (optional, but keeps final image small) --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# copy only maven wrapper + pom and download deps to make build cache-friendly (optional)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -q -B dependency:go-offline

# copy source and build jar
COPY src ./src
RUN ./mvnw -q -B package -DskipTests

# -------- runtime stage --------
FROM eclipse-temurin:21-jre
ARG JAR_FILE=target/customer-service-0.0.1-SNAPSHOT.jar
WORKDIR /app

# add a non-root user
RUN addgroup --system appgroup && adduser --system appuser && chown appuser:appgroup /app
USER appuser

# copy jar from build stage (if you used build stage)
COPY --from=build /app/${JAR_FILE} app.jar

# expose port
EXPOSE 8080

# run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

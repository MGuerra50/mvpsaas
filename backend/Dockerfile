# ---------- Estágio de build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache de dependências
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
RUN mvn -q -B dependency:go-offline

# Compilação
COPY src/ src/
RUN mvn -q -B clean package -DskipTests

# ---------- Estágio de runtime ----------
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

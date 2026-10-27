# ===== 1단계: 빌드 (JDK로 jar 파일 만들기) =====
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Gradle 설정 파일만 먼저 복사 → 의존성 다운로드 (이 단계는 캐시됨)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew dependencies --no-daemon > /dev/null 2>&1 || true

# 소스 복사 후 실행 가능한 jar 빌드
COPY src src
RUN ./gradlew bootJar --no-daemon

# ===== 2단계: 실행 (JRE만 있는 가벼운 이미지) =====
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV TZ=Asia/Seoul
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
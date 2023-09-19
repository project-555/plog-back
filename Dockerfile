# Gradle을 기반으로 하는 Java 프로젝트를 빌드하는 단계
FROM gradle:7.5-jdk18 AS build
WORKDIR /workspace/app

# Gradle wrapper와 설정 파일을 복사합니다.
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 의존성 다운로드를 위해 tasks를 실행하여 캐시를 최적화합니다.
RUN ./gradlew clean build --no-daemon

# 소스코드를 복사하고 애플리케이션을 빌드합니다. (테스트는 제외)
COPY src src
RUN ./gradlew build --no-daemon -x test

# 최종 실행 단계
FROM openjdk:18-slim
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
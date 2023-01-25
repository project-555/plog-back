
.PHONY: build
build:
	./gradlew build --stacktrace -x  test

.PHONY: run
run:
	./gradlew build --stacktrace -x  test
	java -jar ./build/libs/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=local

.PHONY: test
test:
	@./gradlew test --stacktrace
	@./gradlew jacocoTestReport
	@open ./build/reports/tests/test/index.html
	@open ./build/reports/jacoco/test/html/index.html


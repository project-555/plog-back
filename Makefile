
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

.PHONY: ssh-rds-tunnel
ssh-rds-tunnel:
	@ssh -v -N -L 5433:plog-postgres.csqxqxmuxo9l.ap-northeast-2.rds.amazonaws.com:5432 plog-bastion

.PHONY: ssh-redis-tunnel
ssh-redis-tunnel:
	@ssh -v -N -L 6380:plog-redis-001.hk2f3y.0001.apn2.cache.amazonaws.com:6379 plog-bastion
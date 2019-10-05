[![Build Status](https://travis-ci.com/st-spring-samples/spring-batch-unit-testing.svg?branch=master)](https://travis-ci.com/st-spring-samples/spring-batch-unit-testing)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.sudhirt.practice.batch%3Aspring-batch-unit-test&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.sudhirt.practice.batch%3Aspring-batch-unit-test)
# Spring batch - unit testing
Sample project that demonstrates unit testing for batch job built using Spring Batch.

## Prerequisites
-  [JDK 11](https://adoptopenjdk.net/releases.html?variant=openjdk11&jvmVariant=hotspot)
-  [Maven 3.x](https://maven.apache.org/download.cgi)
-  [Git client](https://git-scm.com/download)

## How to start
Clone this repo
```
git clone git@github.com:st-spring-batch-samples/unit-testing.git
```

## Run unit tests
    mvn clean test

## Modules
For additional details, refer to individual module documentation
- [account-service](./account-service/README.md)
- [transaction-batch](./transaction-batch/README.md)

## Source formatting
[Spring Java Format](https://github.com/spring-io/spring-javaformat) plugin is used in this sample for source code formatting. If changes are made to source code, run `mvn spring-javaformat:apply` to reformat code.
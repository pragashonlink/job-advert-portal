## How to run the tests?
- start docker compose `docker-compose up -d`
- source test-env
- ./gradlew clean test

## How to run the project?
- start docker compose `docker-compose up -d`
- source local-env
- ./gradlew bootrun

## Swagger ui for testing
Spin up the project and access `http://localhost:8080/documentation`

## Test data
By default if you run the project in `local` profile then we have data seeded automatically. You can test the end point in Swagger URL

### APPLICATION_SUBMITTED Event schema

```
{
    "jobReferenceId": "d3287200-518d-4261-baa1-2307d239f456",
    "applicationReferenceId": "34b074d8-9dd8-4ebf-9a26-d31f96275429",
    "expectedSalary": 120000,
    "eventType": "APPLICATION_SUBMITTED"
}
```

> `jobReferenceId` should be available in the jobs table
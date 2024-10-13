## Publish job advert API specification

Open API Specification

> [Open API Specification](./open-api-specification.yml)

Base URL for the request to the API

> https://client.jobportal.com

Authentication

> The publish job advert is authenticated using the API_KEY. These are unique tokens provided upon registration.
>> X-Api-Key : {API-KEY}

Versioning

> Must be specified in the URL. Like /api/v1/{resource-path}

Content Type

> application/json

#### Request Headers {#request-headers}
| Name                | Type    |  Description     |
| ------------------  | ------- | ----------------- |
| Content-Type        | string  | 'application/json'    |
| Accept              | string  | 'application/json' |
| X-Api-Key           | string  | API Key provided during <br /> registration |


#### Error Responses {#error-responses}

The response to a request will contain either an error response or a payload in the content type that the endpoint accepts.

| Field       | Type    | Availability   |  Content                 |
|-------------| ------- |----------------| ------------------------ |
| message     | string  | Always present | A human-readable message |

#### Job Status {#job-status}
- Pending
- Published

#### JobPost {#job-post}
| Name                | Type    |  Description     |
| ------------------  | ------- | ----------------- |
| job_id              | string  | UUID to identify the job post    |
| title *              | string  | Job Title    |
| description *     | string  | Job Description |
| numberOfPositions *   | string  | Number of positions to fill |
| status *          | string  | [Job Status](#job-status) |

Request Rate Limiting

> Each client has a limit 100 requests per day

### End points

#### Create a new job post

> POST /jobs

Parameters

Headers: [Request Headers](#request-headers) <br />
Body: [Request Body](#job-post)

API Response Statuses

| Status | Meaning           |  Description                          |  Schema                           |
|--------|-------------------| ------------------------------------- | --------------------------------- |
| 201    | OK                | Interest accrual breakdown returned.  | [Job Post Response](#job-post)    |
| 400    | BadRequest        | A validation error occurred           | [Error Response](#error-response) |
| 401    | UnAuthorized      | Unauthorized                          | [Error Response](#error-response) |
| 404    | NotFound          | Job not found                         | [Error Response](#error-response) |
| 429    | Too many requests | Rate limit exceeded                   | [Error Response](#error-response) |

CURL command

> [Create Job](./create_job_command.curl)

#### Update job post

> PUT /jobs/{job_id}

Parameters

Headers: [Request Headers](#request-headers) <br />
Body: [Request Body](#job-post)

API Response Statuses

| Status                | Meaning       |  Description     |  Schema                 |
| ------------------  |---------------| ----------------- | ------------------------ |
| 204        | No Content    |     | No Response      |
| 400              | BadRequest    | A validation error occurred | [Error Response](#error-response) |
| 401              | UnAuthorized  | Unauthorized | [Error Response](#error-response) |
| 404              | Job Not Found | NotFound | [Error Response](#error-response) |
| 429    | TooManyRequest | Rate limit exceeded | [Error Response](#error-response) |

CURL command

> [Update Job](./update_job_command.curl)

#### Delete job post

> DELETE /jobs/{job_id}

Parameters

Headers: [Request Headers](#request-headers) <br />

API Response Statuses

| Status | Meaning        | Description         |  Schema                 |
|--------|----------------|---------------------| ------------------------ |
| 204    | No Content     |                     | No Response      |
| 401    | UnAuthorized   | Unauthorized        | [Error Response](#error-response) |
| 404    | Job Not Found  | NotFound            | [Error Response](#error-response) |
| 429    | TooManyRequest | Rate limit exceeded | [Error Response](#error-response) |

CURL command

> [Delete Job](./delete_job_command.curl)

#### Read job post

> GET /jobs/{job_id}

Parameters

Headers: [Request Headers](#request-headers) <br />

API Response Statuses

| Status | Meaning        | Description         |  Schema                 |
|--------|----------------|---------------------| ------------------------ |
| 200    | OK             | Job Details         | [Job Post Response](#job-post)       |
| 401    | UnAuthorized   | Unauthorized        | [Error Response](#error-response) |
| 404    | Job Not Found  | NotFound            | [Error Response](#error-response) |
| 429    | TooManyRequest | Rate limit exceeded | [Error Response](#error-response) |

CURL command

> [Read Job](./read_job_command.curl)

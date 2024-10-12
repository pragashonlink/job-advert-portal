## How Jobs are listed?

Upon a job advert is posted successfully through the `JobPublishingService` it emits an `JobPublishedEvent` with the job related payload.

```
{
    job_reference_id:,
    title:,
    description:,
    number_of_positions:,
    status:
}
```
After receiving this event the `JobListingService` will capture the event data and store this information in Elasticsearch database.

### JobPublishedEvent flow

![JobPublished-event-flow](./resources/job_published_event.png)
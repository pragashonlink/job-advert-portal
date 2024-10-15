CREATE TABLE applications
(
    id                          BIGSERIAL PRIMARY KEY,
    job_id                      BIGSERIAL REFERENCES jobs,
    application_reference_id    VARCHAR(100) NOT NULL,
    expected_salary             DECIMAL(22, 4),
    created_at                  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);


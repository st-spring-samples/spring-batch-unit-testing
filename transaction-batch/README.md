# Transaction batch
Sample multi-step batch job to process provided transaction file.

# Batch steps
Transaction processing has been split into two separate steps mentioned below
- Step 1 - Read transactions from flat file and write them to a staging table.
- Step 2 - Read transactions from staging table and process them. If one or more transactions are not processed due to an error, when batch job is restarted, this step will resume and attempts to processing pending transactions.

## Dependencies
Listed below are some of the libraries/frameworks used in this application. Refer to [pom.xml](./pom.xml) for complete details.
- Spring Boot
- Spring Batch
- Lombok
- assertj

## Features covered with unit tests
- Batch processing
- Error handling
- Restartability
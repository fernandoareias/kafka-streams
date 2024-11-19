# Kafka Streams POC

This project is a Proof of Concept (POC) that demonstrates the use of Kafka Streams and KTables. It includes a streaming system with a producer and a stream application, both utilizing Kafka to manage and process real-time data. Kafka Streams is used for data transformation, while KTable efficiently handles state management.

## Project Structure

- **kafka-simple-producer**: A module responsible for generating and sending messages to Kafka.
- **kafka-simple-stream**: A module that consumes messages from Kafka and applies stream operations using Kafka Streams and KTable for data processing and storage.

## Prerequisites

Ensure you have the following installed:

- **Docker** and **Docker Compose**: To create and manage the containers.
- **Java 21** and **Maven**: To compile and build the modules.

## Running the Project

### Step 1: Clone the Repository

```bash
git clone https://github.com/fernandoareias/kafka-streams.git
cd kafka-streams
```

### Step 2: Compile the Modules

The build is handled directly within the Docker containers, so you can proceed with the docker-compose command below.

### Step 3: Run Docker Compose

This command will build the Docker images for each module and start all required containers for the project.

```bash
docker-compose up --build
```

### Step 4: Check the Logs
Once docker-compose is running, you can monitor the service logs to verify that data is being produced and consumed correctly.

### Overview of docker-compose.yml

The docker-compose.yml file configures the following services:

- ```producer-app```: Contains the producer code that generates and sends messages to Kafka.
- ```stream-app```: Consumes messages from Kafka, applies streaming transformations using Kafka Streams, and manages state with KTable.
- ```redpanda-console```: A console for viewing Kafka messages and observing stream operations.
- ```kafka1, kafka2, kafka3```: Kafka clusters configured to simulate a production environment.
schema-registry: Manages Avro schema registration for message serialization/deserialization.

#### Additional Commands

Stop the containers:

```bash
docker-compose down
```


Rebuild and run a specific service (e.g., producer-app):

```bash
docker-compose up --build producer-app
```

### Features

Message Production: producer-app sends simulated data to Kafka.
Stream Processing: stream-app consumes data from Kafka, applies transformations, and uses KTable to manage state.

### Operational Flow Example

```producer-app``` sends messages to a Kafka topic.

```stream-app``` reads messages from the topic, applies a transformation with Kafka Streams, and stores the result in a KTable.

You can view the results in ```redpanda-console``` or monitor the operations in the service logs.
This project provides a straightforward introduction to basic Kafka Streams and KTable usage, serving as a solid foundation for building more complex and efficient data pipelines.


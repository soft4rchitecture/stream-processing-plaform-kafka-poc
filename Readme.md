# PoC: Apache Kafka as a streaming platform

## What's that project all about ?
It's all about experimenting with the stream processing capabilities of Kafka.

## Architecture

The project is made up of 3 components:
- A Consumer, processing all events  received from kafka on a queue named "integer"
- A producer, that will produce 100 events in the queue named "integer"
- Kafka, the Message Oriented Middleware component

## Getting started

### Pre-requisites

- install podman or docker
- install podman-compose

### Steps
1. run `podman machine start` if not done yet. 
2. point your shell to the root of the project and run `podman-compose up -d`. This will bring kafka up.
3.  apply script `./scripts/kafka-topics-create-and-list.sh`


## Sources

- https://developer.ibm.com/tutorials/developing-a-streams-processor-with-apache-kafka/
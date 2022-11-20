#!/bin/bash

 ./scripts/bin/kafka-topics.sh --create --bootstrap-server localhost:29092 --replication-factor 1 --partitions 1 --topic integer
 ./scripts/bin/kafka-topics.sh --create --bootstrap-server localhost:29092 --replication-factor 1 --partitions 1 --topic odd
 ./scripts/bin/kafka-topics.sh --create --bootstrap-server localhost:29092 --replication-factor 1 --partitions 1 --topic even

  ./scripts/bin/kafka-topics.sh --list --bootstrap-server localhost:29092
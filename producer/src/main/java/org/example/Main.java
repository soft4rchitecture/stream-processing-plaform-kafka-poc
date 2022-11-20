package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.stream.IntStream;

public class Main {
    public static final String INTEGER_TOPIC_NAME = "integer";
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (Producer<Integer, String> producer = new KafkaProducer<>(props)) {

            IntStream.rangeClosed(1, 100).forEach((int i) ->
                    producer.send(new ProducerRecord<>(INTEGER_TOPIC_NAME,
                            Integer.valueOf(i), "Value - " + i)));
            for (int i = 0; i < 10; i++) {
                producer.send(new ProducerRecord<>(INTEGER_TOPIC_NAME,
                        Integer.valueOf(i), "Value - " + i));
            }
        }
    }
}
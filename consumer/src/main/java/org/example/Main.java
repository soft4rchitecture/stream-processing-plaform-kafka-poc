package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Main {

    public static final String INTEGER_TOPIC_NAME = "integer";
    public static final String EVEN_TOPIC_NAME = "even";
    public static final String ODD_TOPIC_NAME = "odd";


    public static void main(String[] args) {
        final KafkaStreams stream = new KafkaStreams(createTopology(), createProperties());
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stream.close();
            countDownLatch.countDown();
        }));

        stream.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }

    public static Properties createProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "even-odd-branch");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

    public static Topology createTopology() {
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder.<Integer, String>stream(INTEGER_TOPIC_NAME).split()
                .branch(Main::isEven,
                        Branched.withConsumer(
                                ks -> ks.peek(printEven())
                                        .mapValues(toUpperCase())
                                        .to(EVEN_TOPIC_NAME))
                )
                .branch((key, value) -> true,
                        Branched.withConsumer(
                                ks -> ks.peek(printOdd())
                                        .mapValues(toLowerCase())
                                        .to(ODD_TOPIC_NAME))
                );

        return streamsBuilder.build();

    }

    private static ValueMapper<String, String> toLowerCase() {
        return String::toLowerCase;
    }

    private static ForeachAction<? super Integer, ? super Object> printOdd() {
        return (key, value) -> System.out.printf("odd: %s, %s%n", key, value);
    }

    private static ValueMapper<String, String> toUpperCase() {
        return String::toUpperCase;
    }

    private static ForeachAction<Integer, Object> printEven() {
        return (key, value) -> System.out.printf("even: %s, %s%n", key, value);
    }

    private static Boolean isEven(Integer key, Object value) {
        return key % 2 == 0;
    }


}
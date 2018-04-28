package me.ktwo.kfk_pojo;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class BookKfkConsumer {

    private KafkaConsumer<String, String> consumer;


    public BookKfkConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
    }

    void consume(){
        consumer.subscribe(Arrays.asList(BookKfkProducer.TOPIC));
        System.out.println("in topic");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(500);;
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Book book = mapper.readValue(record.value(),Book.class);
                    System.out.println(book);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        new BookKfkConsumer().consume();
    }


}

package me.ktwo.kfk.consumer;


import me.ktwo.kfk.producer.MyKfkProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 手动提交
 */
public class MyKfkConsumer2 {

    private KafkaConsumer<String, String> consumer;


    public MyKfkConsumer2() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094");
        props.put("group.id", "test");
 //     props.put("enable.auto.commit", "true");//自动提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");//自动提交
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
    }

    void consume(){
        consumer.subscribe(Arrays.asList(MyKfkProducer.TOPIC));
        System.out.println("in topic");
        final int minBatchSize = 10;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);;
            for (ConsumerRecord<String, String> record : records){
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
                // TODO handle buffer
                consumer.commitSync(); //手动提交，保证处理时不出问题
                buffer.clear();
            }
        }
    }
    public static void main(String[] args) {
        new MyKfkConsumer2().consume();
    }


}

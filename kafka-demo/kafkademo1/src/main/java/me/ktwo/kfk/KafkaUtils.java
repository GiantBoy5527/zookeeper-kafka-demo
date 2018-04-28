package me.ktwo.kfk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaUtils {
    private static KafkaProducer<Integer, String> producer;
    public final static String TOPIC = "test1";

    public KafkaUtils() {
    }

    static {
        Properties properties=new Properties();
        properties.put("bootstrap.servers", "127.0.0.1:9092");
        properties.put("metadata.broker.list", "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094");
        properties.put("zk.connect", "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("request.required.acks", "1");
        producer = new KafkaProducer<Integer, String>(properties);
    }

    /**
     * 发送对象消息 至kafka上,调用json转化为json字符串，应为kafka存储的是String。
     * @param obj
     */
    public static void sendMsgToKafka(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String msg=mapper.writeValueAsString(obj);
            producer.send(new ProducerRecord<Integer, String>(TOPIC, msg));
        } catch (Exception e) {

        } finally {
            //关闭资源
            producer.close();
        }
    }
}

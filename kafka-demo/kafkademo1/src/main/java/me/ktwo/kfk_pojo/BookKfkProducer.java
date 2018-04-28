package me.ktwo.kfk_pojo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class BookKfkProducer {
    private  final Producer<String,String> kafkaProducer;

    public final static String TOPIC="booktopic";

    private BookKfkProducer(){
        kafkaProducer=this.createKafkaProducer() ;
    }
    private Producer<String,String> createKafkaProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.5.108:9092,192.168.5.108:9093,192.168.5.108:9094");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 10);// 批次数量
        props.put("linger.ms", 1);
        props.put("buffer.memory", 1024);// 缓冲数量
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> kafkaProducer = new KafkaProducer<>(props);
        System.out.println("createKafkaProducer over");
        return kafkaProducer;
    }

    void produce()  {

        for(int i=1;i<1000;i++){
            Book book =new Book();
            book.setId(i);
            book.setName("开发者指南"+i);
            book.setPrice(50.35+i);
            book.setSale(i%2==0);
            book.setRemark("say:"+(char)i);
            book.setC((char)i);
            String json="";
            try {
                Thread.sleep(1000);
                ObjectMapper mapper = new ObjectMapper();
                json=mapper.writeValueAsString(book);
            } catch (InterruptedException | JsonProcessingException e) {
                e.printStackTrace();
            }

            String key=String.valueOf("key"+i);

            kafkaProducer.send(new ProducerRecord<>(TOPIC, key, json), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // 返回值慢，创建topic错误，最好手动创建
                    if(recordMetadata != null){
                        System.out.println(recordMetadata.toString()+"<-->"+recordMetadata.hasTimestamp());
                    }
                    if(e !=null){
                        e.printStackTrace();
                    }
                }
            });
            System.out.println(key+":"+json);
        }
    }

    public  static  void main(String[] args) throws JsonProcessingException {
        new BookKfkProducer().produce();
    }
}


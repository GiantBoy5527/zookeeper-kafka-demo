package me.ktwo.kfk.producer;





import org.apache.kafka.clients.producer.*;



import java.util.Properties;

public class MyKfkProducer {
    private  final Producer<String,String> kafkaProducer;

    public final static String TOPIC="testtopic";

    private MyKfkProducer(){
        kafkaProducer=createKafkaProducer() ;
    }
    private Producer<String,String> createKafkaProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.5.108:9092,192.168.5.108:9093,192.168.5.108:9094");
        //props.put("acks", "all");
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put("retries", 3);
        props.put("batch.size", 5);// 批次数量
        props.put("linger.ms", 1);
        props.put("buffer.memory", 512);// 缓冲数量
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> kafkaProducer = new KafkaProducer<>(props);
        System.out.println("createKafkaProducer over");
        return kafkaProducer;
    }

    void produce(){
        for(int i=1;i<1000;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String key=String.valueOf("key"+i);
            String data="hello kafka message:"+key;
            System.out.println("in send "+i);
            kafkaProducer.send(new ProducerRecord<>(TOPIC, key, data), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // 返回值慢，创建topic错误，最好手动创建
                    if(recordMetadata != null){
                        System.out.println(recordMetadata.toString());
                        System.out.println(recordMetadata.hasTimestamp());
                    }
                    if(e !=null){
                        e.printStackTrace();
                    }
                }
            });
            System.out.println(data);
        }
    }

    public  static  void main(String[] args){
        new MyKfkProducer().produce();
    }
}


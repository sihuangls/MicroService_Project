package com.food.ordering.system.order.service.messaging.publisher.kafka;


import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class OrderKafkaMessageHelper {

    //因为我们发现，无论是哪个messagepublisher,都会使用这个callbackfunction,直接将其解耦
    //但pay哪里我们发现不能单单使用PaymentRequestAvroModel了，需要restaurantApprovalRequestAvroModel
    //因此替换成泛型
    //这样就发现找不到orderId了，所以必须引入
    public <T>ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(String ResponseTopicName, T RequestAvroModel, String orderId, String requestAvroModelName) {
        return new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending " + requestAvroModelName +
                        "message {} to topic {}", RequestAvroModel.toString(), ResponseTopicName, ex);
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received successful response from Kafka for order id: {}"+
                                " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                        orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp()
                );
            }
        };
    }
}

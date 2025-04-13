package org.github.babkiniaa.scas.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        return new KafkaAdmin(configs);
    }

    @Bean
    NewTopic createTopicReport() {
        return TopicBuilder.name("report-create-events-topic").partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic createTopicTask() {
        return TopicBuilder.name("task-create-events-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic loadTopicTask() {
        return TopicBuilder.name("task-load-events-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic createTopicReportDLT() {
        return TopicBuilder.name("report-create-events-topic.DLT")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic createTopicTaskDLT() {
        return TopicBuilder.name("task-create-events-topic.DLT")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic loadTopicTaskDLT() {
        return TopicBuilder.name("task-load-events-topic.DLT")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            @Autowired KafkaTemplate<String, Object> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, exception) -> new TopicPartition(record.topic() + ".DLT", record.partition())
                ),
                new FixedBackOff(1000L, 3)
        );

        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}

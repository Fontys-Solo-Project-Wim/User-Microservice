package y.userservice.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogPublisher {
    @Value("${rabbitmq.log.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.log.routing.key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public LogPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishLog(String logMessage) {
        LOGGER.info(String.format("Log message published to RabbitMQ -> %s", logMessage));
        rabbitTemplate.convertAndSend(exchange, routingKey, logMessage);
    }
}

package smsservice.cfgs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import smsservice.service.QueueContainerService;

@Slf4j
@Configuration
public class RabbitConfiguration {

    private SimpleMessageListenerContainer container;

    @Bean
    public MessageConverter makeMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MyRabbitListenerContainerFactory makeRabbitListenerContainerFactory() {
        return new MyRabbitListenerContainerFactory();
    }

    @Bean
    public ConnectionFactory connectionFactory(
            @Value("${RABBIT_HOST}") String host,
            @Value("${RABBIT_USER}") String user,
            @Value("${RABBIT_PASS}") String pass,
            @Value("${RABBIT_PORT}") String port
    )
    {
        var connectionFactory = new CachingConnectionFactory(host, Integer.parseInt(port));
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(pass);
        return connectionFactory;
    }

    @Bean
    public QueueContainerService makeContainerService(MyRabbitListenerContainerFactory factory) {
        return new QueueContainerService() {
            @Override
            public void start() {
                factory.getContainer().start();
            }
            @Override
            public void stop() {
                factory.getContainer().stop();
            }
            @Override
            public boolean isStarted() {
                return factory.getContainer().isRunning();
            }
        };
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MyRabbitListenerContainerFactory factory
    ) {
        factory.setMessageConverter(makeMessageConverter());
        factory.setConnectionFactory(connectionFactory);
        factory.setMaxConcurrentConsumers(5);
        log.info("Container started");
        return factory;
    }

}

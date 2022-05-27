package smsservice.cfgs;

import lombok.Getter;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

public class MyRabbitListenerContainerFactory extends SimpleRabbitListenerContainerFactory {
    @Getter
    private SimpleMessageListenerContainer container;
    @Override
    protected SimpleMessageListenerContainer createContainerInstance() {
        if (null == container)
            container = super.createContainerInstance();
        return container;
    }
}
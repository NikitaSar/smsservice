package smsservice;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import smsservice.model.SmsMessage;
import smsservice.service.smsprovider.SmsBalanceResponse;
import smsservice.service.smsprovider.SmsProvider;
import smsservice.service.smsprovider.SmsResponse;
import smsservice.service.smsprovider.SmsStatusCode;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(initializers = IntegrationTesBase.Initializer.class)
public class SmsControllerIntegrationTest extends IntegrationTesBase {

    @Autowired
    public SmsProvider smsProvider;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    public Queue queue;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public  SmsProvider mockSmsProvider() throws Exception {
            var smsProvider = mock(SmsProvider.class);
            var res = new SmsResponse(SmsStatusCode.OK);
            given(smsProvider.authorization()).willReturn(res);
            return smsProvider;
        }

        @Bean
        public Queue hello() {
            return new Queue(rabbitQueueName);
        }

        @Primary
        @Bean
        public RabbitTemplate makeRabbitTemplate() {
            var cf = new CachingConnectionFactory();
            cf.setHost(rabbitHost);
            cf.setPort(rabbitPort);
            cf.setUsername(rabbitUser);
            cf.setPassword(rabbitPass);
            var rabbitAdmin = new RabbitAdmin(cf);
            rabbitAdmin
                    .getRabbitTemplate()
                    .setMessageConverter(new Jackson2JsonMessageConverter());
            return rabbitAdmin.getRabbitTemplate();
        }
    }

    @Test
    public void queueListen() throws Exception {
        var res = new SmsBalanceResponse(10f);
        given(smsProvider.getBalance()).willReturn(res);
        given(smsProvider.send("2222", "text")).willReturn(res);
        var sms = new SmsMessage("2222", "text");
        rabbitTemplate.convertAndSend(queue.getName(), sms);
        Thread.sleep(2000);
        verify(smsProvider).send("2222", "text");
    }

}

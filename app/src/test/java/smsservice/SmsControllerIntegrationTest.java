package smsservice;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import smsservice.model.SmsMessage;
import smsservice.service.smsprovider.SmsBalanceResponse;
import smsservice.service.smsprovider.SmsProvider;
import smsservice.service.smsprovider.SmsResponse;
import smsservice.service.smsprovider.SmsStatusCode;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class SmsControllerIntegrationTest extends IntegrationTesBase {

    @Autowired
    public SmsProvider smsProvider;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    public Queue queue;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public  SmsProvider mockSmsProvider() throws Exception {
            var smsProvider = mock(SmsProvider.class);
            var res = new SmsResponse();
            res.setCode(SmsStatusCode.OK);
            given(smsProvider.authorization()).willReturn(res);
            return smsProvider;
        }

        @Bean
        public Queue hello() {
            return new Queue("sms");
        }

        @Primary
        @Bean
        public RabbitAdmin makeRabbitAdmin() {
            var cf = new CachingConnectionFactory();
            cf.setHost(rabbitHost);
            cf.setPort(rabbitPort);
            cf.setUsername(rabbitUser);
            cf.setPassword(rabbitPass);
            return new RabbitAdmin(cf);
        }
    }

    @Test
    public void queueListen() throws Exception {
        var res = new SmsBalanceResponse();
        res.setCode(SmsStatusCode.OK);
        res.setBalance(10f);
        given(smsProvider.getBalance()).willReturn(res);
        given(smsProvider.send("2222", "text")).willReturn(res);
        rabbitAdmin.getRabbitTemplate().setMessageConverter(new Jackson2JsonMessageConverter());
        var sms = new SmsMessage("2222", "text");
        rabbitAdmin.getRabbitTemplate().convertAndSend(queue.getName(), sms);
        Thread.sleep(2000);
        verify(smsProvider).send("2222", "text");
    }

}

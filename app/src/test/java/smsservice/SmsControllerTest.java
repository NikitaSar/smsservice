package smsservice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.springframework.amqp.AmqpException;
import smsservice.cfgs.AppConfiguration;
import smsservice.controller.SmsController;
import smsservice.model.SmsMessage;
import smsservice.service.QueueContainerService;
import smsservice.service.SmsService;

public class SmsControllerTest {

    QueueContainerService containerService = mock(QueueContainerService.class);
    final String queueName = "sms";

    @Test
    public void notifyAdminWhenBalanceLessThreshold() throws Exception {
        var expectedAdminPhone = "123456789";
        var expectedMessage = "Balance: 9.00";
        var smsService = mock(SmsService.class);
        given(smsService.authorization()).willReturn(true);
        given(smsService.getBalance()).willReturn(9f);

        var smsCtrl = new SmsController(containerService, smsService,
                new AppConfiguration(10, expectedAdminPhone, queueName));

        assertThrows(AmqpException.class, ()-> smsCtrl.listen(new SmsMessage("123", "Text")));
        verify(smsService).send(expectedAdminPhone, expectedMessage);
    }

    @Test
    public void queueListenSuccess() throws Exception {
        var expectedMessage = new SmsMessage("12345678", "Text message");
        var smsService = mock(SmsService.class);
        given(smsService.authorization()).willReturn(true);
        given(smsService.getBalance()).willReturn(6f);

        new SmsController(containerService, smsService,
            new AppConfiguration(5, "", queueName))
            .listen(expectedMessage);
        verify(smsService).send(expectedMessage.getPhone(), expectedMessage.getMsg());
    }

    @Test
    public void smsServiceAuthException() throws Exception {
        SmsService smsService = mock(SmsService.class);
        given(smsService.authorization()).willReturn(false);
        assertThrows(Exception.class, () -> {
            new SmsController(containerService, smsService,
                    new AppConfiguration(0, "", ""))
                    .listen(new SmsMessage());
        });
    }
}

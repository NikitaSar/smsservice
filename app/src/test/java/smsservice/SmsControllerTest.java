package smsservice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.amqp.AmqpException;
import smsservice.cfgs.AppConfiguration;
import smsservice.controller.SmsController;
import smsservice.mocks.FakeQueueContainerService;
import smsservice.mocks.FakeSmsService;
import smsservice.model.SmsMessage;
import smsservice.service.QueueContainerService;

import java.util.concurrent.atomic.AtomicBoolean;

public class SmsControllerTest {

    QueueContainerService containerService = new FakeQueueContainerService();
    final String queueName = "sms";

    @Test
    public void notifyAdminWhenBalanceLessThreshold() throws Exception {
        var expectedAdminPhone = "123456789";
        var expectedMessage = "Balance: 9.00";
        AtomicBoolean flag = new AtomicBoolean(false);
        var smsService = new FakeSmsService(9, "token", (phone, msg) -> {
            flag.set(true);
            assertEquals(expectedAdminPhone, phone);
            assertEquals(expectedMessage, msg);
        });
        var smsCtrl = new SmsController(containerService, smsService,
                new AppConfiguration(10, expectedAdminPhone, queueName));

        assertThrows(AmqpException.class, ()-> smsCtrl.listen(new SmsMessage("123", "Text")));
        assertTrue(flag.get());
    }

    @Test
    public void queueListenSuccess() throws Exception {
        AtomicBoolean flag = new AtomicBoolean(false);
        var expectedMessage = new SmsMessage("12345678", "Text message");
        var smsService = new FakeSmsService(10, "token", (phone, msg) -> {
            flag.set(true);
            assertEquals(expectedMessage.getPhone(), phone);
            assertEquals(expectedMessage.getMsg(), msg);
        });
        new SmsController(containerService, smsService,
            new AppConfiguration(5, "", queueName))
            .listen(expectedMessage);
        assertTrue(flag.get());
    }

    @Test
    public void smsServiceAuthException() {
        var smsService = new FakeSmsService(0, "expected token", (phone, msg) -> {});

        assertThrows(Exception.class, () -> {
            new SmsController(containerService, smsService,
                    new AppConfiguration(0, "", ""))
                    .listen(new SmsMessage());
        });
    }
}

package smsservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import smsservice.cfgs.AppConfiguration;
import smsservice.model.SmsMessage;
import smsservice.service.QueueContainerService;
import smsservice.service.SmsService;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class SmsController {
    private final SmsService smsService;
    private final float thresholdBalance;
    private final String adminPhone;
    private final QueueContainerService containerService;

    public SmsController(QueueContainerService containerService,
                         SmsService smsService,
                         AppConfiguration app
    ) throws Exception {
        this.containerService = containerService;
        this.smsService = smsService;
        this.thresholdBalance = app.getThresholdBalance();
        this.adminPhone = app.getAdminPhone();
        if (!smsService.authorization()) {
            throw new Exception("Unresolved sms token.");
        }
    }

    @RabbitListener(queues = "sms", containerFactory = "myRabbitListenerContainerFactory")
    public void listen(SmsMessage data) throws Exception {
        log.info("Received '{}' from queue.", data);
        var balance = smsService.getBalance();
        if (balance < thresholdBalance) {
            containerService.stop();
            var msg = String.format(Locale.US, "Balance: %.2f", balance);
            smsService.send(adminPhone, msg);
            throw new AmqpException(msg);
        }
        smsService.send(data.getPhone(), data.getMsg());
    }

    @Scheduled(fixedDelay = 60_000)
    public void checkConnection() throws Exception {
        if (!containerService.isStarted()) {
            if (smsService.getBalance() > thresholdBalance)
                containerService.start();
        }
    }
}

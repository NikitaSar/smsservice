package smsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smsservice.service.smsprovider.SmsProvider;
import smsservice.service.smsprovider.SmsStatusCode;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final SmsProvider smsProvider;

    @Override
    public float getBalance() throws Exception {
        var res = smsProvider.getBalance();
        checkStatusCode(res.getCode());
        return res.getBalance();
    }

    @Override
    public boolean authorization() throws Exception {
        return smsProvider
                .authorization()
                .getCode() == SmsStatusCode.OK;
    }

    @Override
    public void send(String phone, String msg) throws Exception {
        var res = smsProvider.send(phone, msg);
        checkStatusCode(res.getCode());
    }

    private void checkStatusCode(SmsStatusCode code) throws Exception {
        if (code != SmsStatusCode.OK)
            throw new Exception(String.format("Sms service responses status code %d", code.getValue()));
    }
}

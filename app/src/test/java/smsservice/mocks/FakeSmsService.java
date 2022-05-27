package smsservice.mocks;

import lombok.RequiredArgsConstructor;
import org.testcontainers.shaded.org.apache.commons.lang3.NotImplementedException;
import smsservice.service.SmsService;

@RequiredArgsConstructor
public class FakeSmsService implements SmsService {
    private final float balance;
    private final String token;
    private final SendSmsCallback callback;

    @Override
    public float getBalance() throws Exception {
        return balance;
    }

    @Override
    public boolean authorization() throws Exception {
        //throw new NotImplementedException();
        return true;
        //return this.token.equals(token);
    }

    @Override
    public void send(String phone, String msg) throws Exception {
        callback.send(phone, msg);
    }

    public interface SendSmsCallback {
        void send(String phone, String msg);
    }
}

package smsservice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import smsservice.service.SmsService;
import smsservice.service.SmsServiceImpl;
import smsservice.service.smsprovider.SmsBalanceResponse;
import smsservice.service.smsprovider.SmsProvider;
import smsservice.service.smsprovider.SmsResponse;
import smsservice.service.smsprovider.SmsStatusCode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SmsServiceImplTest {

    SmsProvider smsProvider = mock(SmsProvider.class);
    SmsService smsService = new SmsServiceImpl(smsProvider);

    @Test
    public void getBalanceSuccess() throws Exception {
        var expected = new SmsBalanceResponse();
        expected.setBalance(100f);
        expected.setCode(SmsStatusCode.OK);
        given(smsProvider.getBalance()).willReturn(expected);
        var actual = smsService.getBalance();

        assertEquals(expected.getBalance(), actual);
    }

    @Test
    public void getBalanceNotAuthorized() throws Exception {
        var notAuthResponse = new SmsBalanceResponse();
        notAuthResponse.setCode(SmsStatusCode.NOT_AUTHORIZED);
        given(smsProvider.getBalance()).willReturn(notAuthResponse);
        assertThrows(Exception.class, () -> {
            smsService.getBalance();
        });
    }

    @Test
    public void notAuthorized() throws Exception {
        var notAuthResponse = new SmsResponse();
        notAuthResponse.setCode(SmsStatusCode.NOT_AUTHORIZED);
        given(smsProvider.authorization()).willReturn(notAuthResponse);
        assertFalse(smsService.authorization());
    }

    @Test
    public void authorizationSuccess() throws Exception {
        var successRes = new SmsResponse();
        successRes.setCode(SmsStatusCode.OK);
        given(smsProvider.authorization()).willReturn(successRes);
        assertTrue(smsService.authorization());
    }

    @Test
    public void sendSuccess() throws Exception {
        var phone = "123";
        var msg = "text message";
        var successRes = new SmsResponse();
        successRes.setCode(SmsStatusCode.OK);
        given(smsProvider.send(phone, msg)).willReturn(successRes);
        smsService.send(phone, msg);
    }

}

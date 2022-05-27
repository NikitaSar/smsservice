package smsservice.service.smsprovider;

public interface SmsProvider {
    SmsResponse authorization() throws Exception;
    SmsBalanceResponse getBalance() throws Exception;
    SmsResponse send(String phone, String msg) throws Exception;
}

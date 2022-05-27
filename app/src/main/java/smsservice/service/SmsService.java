package smsservice.service;

public interface SmsService {
    float getBalance() throws Exception;
    boolean authorization() throws Exception;
    void send(String phone, String msg) throws Exception;
}

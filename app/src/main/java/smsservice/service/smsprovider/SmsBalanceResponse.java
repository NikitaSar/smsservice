package smsservice.service.smsprovider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SmsBalanceResponse extends SmsResponse {
    private float balance;

    public SmsBalanceResponse(SmsStatusCode code) {
        this.code = code;
    }

    public SmsBalanceResponse(float balance) {
        this(SmsStatusCode.OK);
        this.balance = balance;
    }
}

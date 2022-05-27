package smsservice.service.smsprovider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SmsBalanceResponse extends SmsResponse {
    private float balance;
}

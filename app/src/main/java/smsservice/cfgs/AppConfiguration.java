package smsservice.cfgs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class AppConfiguration {
    @Value("${THRESHOLD_BALANCE}")
    private float thresholdBalance;
    @Value("${ADMIN_PHONE}")
    private String adminPhone;
    @Value("${QUEUE_NAME}")
    private String queueName;
  //  @Value("${SMSRU_TOKEN}")
  //  private String smsToken;
}

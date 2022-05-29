package smsservice.service.smsprovider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SmsResponse {
    private String status;
    @JsonProperty("status_code")
    private SmsStatusCode code;
    @JsonProperty("status_text")
    private String message;
}

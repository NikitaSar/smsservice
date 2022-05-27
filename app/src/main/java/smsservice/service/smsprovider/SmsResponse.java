package smsservice.service.smsprovider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmsResponse {
    private String status;
    @JsonProperty("status_code")
    private SmsStatusCode code;
    @JsonProperty("status_text")
    private String message;
}

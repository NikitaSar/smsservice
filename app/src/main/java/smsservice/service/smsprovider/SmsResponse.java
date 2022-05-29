package smsservice.service.smsprovider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SmsResponse {
    protected String status;
    @JsonProperty("status_code")
    protected SmsStatusCode code;
    @JsonProperty("status_text")
    protected String message;

    public SmsResponse(SmsStatusCode code) {
        this.code = code;
    }
}

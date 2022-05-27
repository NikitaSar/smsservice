package smsservice.service.smsprovider;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SmsStatusCode {
    OK(100),
    NOT_AUTHORIZED(200),
    NEGATIVE_BALANCE(201),
    INCORRECT_PHONE(202);

    @JsonValue
    @Getter
    private final Integer value;
}

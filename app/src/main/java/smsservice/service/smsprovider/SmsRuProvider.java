package smsservice.service.smsprovider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import smsservice.Consts;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsRuProvider implements SmsProvider {
    private final RestTemplate restTemplate;
    private final String token;

    public SmsRuProvider(@Value("${SMSRU_TOKEN}") String token, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.token = token;
    }

    @Override
    public SmsResponse authorization() throws Exception {
        return restTemplate.getForEntity(makeUriString("/auth/check"), SmsResponse.class).getBody();
    }

    @Override
    public SmsBalanceResponse getBalance() throws Exception {
        return restTemplate.getForEntity(makeUriString("/my/balance"), SmsBalanceResponse.class).getBody();
    }

    @Override
    public SmsResponse send(String phone, String msg) throws Exception {
        return restTemplate.getForEntity(
                makeUriString("/sms/send)", Map.of("to", phone, "msg", msg)),
                SmsResponse.class).getBody();
    }

    private String makeUriString(String uri) {
        return makeUriString(uri, Map.of());
    }

    private String makeUriString(String uri, Map<String, String> uriParams) {
        if (null == uriParams)
            throw new IllegalArgumentException("uriParams cannot be null.");
        var uriBuilder = UriComponentsBuilder.fromHttpUrl(Consts.SMSRU_BASE_URL + uri)
                .queryParam("api_id", token)
                .queryParam("json", 1);
        uriParams.forEach(uriBuilder::queryParam);

        return uriBuilder.toUriString();
    }
}

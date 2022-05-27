package smsservice.service.smsprovider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import smsservice.Consts;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsRuProvider implements SmsProvider {
    private final RestTemplate restTemplate;
    private final String token;
//    private Map<String, Object> uriVariables;

    public SmsRuProvider(@Value("${SMSRU_TOKEN}") String token) {
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(Consts.SMSRU_BASE_URL));
        //uriVariables = Map.of("api_id", token, "json", 1);
        this.token = token;
    }

    @Override
    public SmsResponse authorization() throws Exception {
        var uri = String.format("/auth/check?api_id=%s&json=1", token);
        var res = restTemplate.getForEntity(uri, SmsResponse.class)
                .getBody();

        return res;
    }

    @Override
    public SmsBalanceResponse getBalance() throws Exception {
        return restTemplate.getForEntity("/my/balance", SmsBalanceResponse.class)
                .getBody();
    }

    @Override
    public SmsResponse send(String phone, String msg) throws Exception {
//        HashMap<String, Object> uriVariablesEx = new HashMap<>(uriVariables);
//        uriVariablesEx.put("to", phone);
//        uriVariablesEx.put("msg", msg);
        return restTemplate.getForEntity("/sms/send", SmsResponse.class)
                .getBody();
    }
}

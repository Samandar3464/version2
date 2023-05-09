package uz.optimit.taxi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.optimit.taxi.exception.SmsSendingFailException;
import uz.optimit.taxi.exception.SmsServiceBroken;
import uz.optimit.taxi.model.request.SmsModel;
import uz.optimit.taxi.model.response.SmsResponse;
import uz.optimit.taxi.model.request.SmsToken;
import uz.optimit.taxi.entity.Token;
import uz.optimit.taxi.repository.TokenRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uz.optimit.taxi.entity.Enum.Constants.CAN_NOT_SEND_SMS;
import static uz.optimit.taxi.entity.Enum.Constants.CAN_NOT_TAKE_SMS_SENDING_SERVICE_TOKEN;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final TokenRepository tokenRepository;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;
    private static final String userEmail = "samandarshodmonov1998@gmail.com";
    private final static String userSecret = "TaEotaLMYaUqqGTgTrzjGnmIFo7whQfaf82vMU8o";

    private static final String GET_TOKEN = "https://notify.eskiz.uz/api/auth/login";
    private static final String RELOAD_TOKEN = "https://notify.eskiz.uz/api/auth/refresh";
    private static final String SMS_SEND = "https://notify.eskiz.uz/api/message/sms/send";

    public String getToken() {
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("email", userEmail);
            requestBody.put("password", userSecret);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody);
            ResponseEntity<String> response = restTemplate.postForEntity(GET_TOKEN, request, String.class);
            String body = response.getBody();
            SmsToken smsToken = objectMapper.readValue(body, SmsToken.class);
            Token build = Token.builder().token(smsToken.getData().getToken()).build();
            Token token = new Token();
            List<Token> all = tokenRepository.findAll();
            if (all.isEmpty()) {
                token = tokenRepository.save(build);
            } else {
                token.setId( all.get(0).getId());
                token.setToken(build.getToken());
                token = tokenRepository.save(token);
            }
            return token.getToken();
        } catch (Exception e) {
            throw new SmsServiceBroken(CAN_NOT_TAKE_SMS_SENDING_SERVICE_TOKEN);
        }
    }

    public SmsResponse sendSms(SmsModel smsModel) {
        HttpEntity<Map<String, String>> request = null;
        HttpHeaders headers = new HttpHeaders();
        String token = null;
        try {
            headers.setContentType(MediaType.APPLICATION_JSON);
            List<Token> all = tokenRepository.findAll();
            if (!all.isEmpty()) {
                token = all.get(0).getToken();
            } else {
                token = getToken();
            }
            headers.set("Authorization", "Bearer " + token);
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("mobile_phone", "998" + smsModel.getMobile_phone());
            requestBody.put("message", smsModel.getMessage());
            requestBody.put("from", String.valueOf(smsModel.getFrom()));
            requestBody.put("callback_url", smsModel.getCallback_url());
            request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(SMS_SEND, request, String.class);
            String body = response.getBody();
            return objectMapper.readValue(body, SmsResponse.class);
        } catch (Exception e) {
            try {
                token = reFreshToken(token);
                HttpHeaders headers1 = request.getHeaders();
                headers1.setBearerAuth(token);
                restTemplate.postForEntity(SMS_SEND, request, String.class);
            } catch (Exception e1) {
                throw new SmsSendingFailException(CAN_NOT_SEND_SMS);
            }
        }
        return null;
    }

    private String reFreshToken(String oldToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + oldToken);
        HttpEntity<?> httpEntity =new HttpEntity<>(headers);
        restTemplate.patchForObject(RELOAD_TOKEN, httpEntity, Void.class);
        return getToken();
    }
}

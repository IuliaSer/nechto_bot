package nechto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TelegramRestSender {

    private final RestTemplate restTemplate;

    @Value("${telegrambot.botToken}")
    private String token;

    private String baseUrl() {
        return "https://api.telegram.org/bot" + token;
    }

    public void sendPhoto(long chatId, byte[] pngBytes, String caption, String filename) {
        var form = new org.springframework.util.LinkedMultiValueMap<String, Object>();
        form.add("chat_id", String.valueOf(chatId));
        if (caption != null && !caption.isBlank()) form.add("caption", caption);

        var filePart = new org.springframework.core.io.ByteArrayResource(pngBytes) {
            @Override public String getFilename() {
                return (filename == null || filename.isBlank()) ? "qr.png" : filename;
            }
            @Override public long contentLength() { return pngBytes.length; }
        };
        form.add("photo", filePart);

        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);

        var requestEntity = new org.springframework.http.HttpEntity<>(form, headers);

        try {
            var resp = restTemplate.postForEntity(baseUrl() + "/sendPhoto", requestEntity, String.class);
            if (!resp.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("Telegram sendPhoto failed: " + resp.getStatusCode() + " "
                        + resp.getBody());
            }
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            throw new IllegalStateException("Telegram sendPhoto error: " + e.getStatusCode() + " " +
                    e.getResponseBodyAsString(), e);
        }
    }

}

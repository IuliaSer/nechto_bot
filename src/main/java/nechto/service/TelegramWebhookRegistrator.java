package nechto.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramWebhookRegistrator {

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  @Value("${telegram.bot.token}")
  private String botToken;

  @Value("${ngrok.apiBase:http://localhost:4040}") //nastroit v dokere tozhe
  private String ngrokApiBase;

  private String baseUrl() {
	return "https://api.telegram.org/bot" + botToken;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void registerWebhook() {
	try {
	  String targetUrl = waitForNgrokHttps();
	  String current = getCurrentWebhookUrl();

	  if (!targetUrl.equals(current)) {
		setWebhook(targetUrl);
		log.debug("[webhook] setWebhook -> " + targetUrl);
	  } else {
		log.debug("[webhook] already set: " + current);
	  }
	} catch (Exception e) {
	  log.error("[webhook] failed to register: " + e.getMessage());
	}
  }

  public void clearUpdates() {
	try {
	  MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
	  form.add("drop_pending_updates", String.valueOf(true));

	  HttpHeaders headers = new HttpHeaders();
	  headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	  HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(form, headers);

	  var resp = restTemplate.postForEntity(baseUrl() + "/deleteWebhook", requestEntity, String.class);
	  if (!resp.getStatusCode().is2xxSuccessful()) {
		throw new IllegalStateException("Clear failed: " + resp.getStatusCode() + " " + resp.getBody());
	  }
	} catch (HttpStatusCodeException e) {
	  throw new IllegalStateException("Telegram sendPhoto error: " + e.getStatusCode() + " " +
			  e.getResponseBodyAsString(), e);
	}
  }

  private String waitForNgrokHttps() throws Exception {
	for (int i = 1; i <= 10; i++) {
	  try {
		String url = ngrokApiBase + "/api/tunnels";
		ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);

		if (resp.getStatusCode().is2xxSuccessful()) {
		  JsonNode root = mapper.readTree(resp.getBody()).path("tunnels");
		  for (JsonNode t : root) {
			String publicUrl = t.path("public_url").asText("");
			if (publicUrl.startsWith("https://")) {
			  return publicUrl;
			}
		  }
		}
	  } catch (Exception ignore) {}
	  Thread.sleep(Duration.ofSeconds(3).toMillis());
	}
	throw new IllegalStateException("ngrok https tunnel not found at " + ngrokApiBase);
  }

  public String getUpdatesAmount() {
	return getParameterFromWebhookInfo("pending_update_count");
  }

  private String getCurrentWebhookUrl() {
	return getParameterFromWebhookInfo("url");
  }

  private String getParameterFromWebhookInfo(String parameter) {
	String url = "https://api.telegram.org/bot" + botToken + "/getWebhookInfo";
	ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
	if (!resp.getStatusCode().is2xxSuccessful()) {
	  return "";
	}
	try {
	  JsonNode root = mapper.readTree(resp.getBody()).path("result");
	  return root.path(parameter).asText("");
	} catch (Exception e) {
	  return "";
	}
  }

  private void setWebhook(String targetUrl) {
	String url = "https://api.telegram.org/bot" + botToken + "/setWebhook";
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	String body = "url=" + targetUrl;
	restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
  }

}

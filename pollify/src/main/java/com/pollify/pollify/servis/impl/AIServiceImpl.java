package com.pollify.pollify.servis.impl;

import com.pollify.pollify.model.Poll;
import com.pollify.pollify.repository.PollRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AIServiceImpl {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final PollRepository pollRepository;
    private final RestTemplate restTemplate;

    public AIServiceImpl(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
        this.restTemplate = new RestTemplate();
    }

    public String analyzePollResults(Long pollId) {
        System.out.println("Anket sonuçları AI için analiz ediliyor: pollId=" + pollId);
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));

        String prompt = buildPromptFromPoll(poll);
        System.out.println("AI prompt hazırlandı:\n" + prompt);

        String result = sendToOpenAI(prompt);
        System.out.println("AI yanıtı alındı.");
        return result;
    }

    private String buildPromptFromPoll(Poll poll) {
        StringBuilder sb = new StringBuilder();
        sb.append("Aşağıda bir anketin detaylı sonuçları vardır:\n");
        sb.append("Anket Başlığı: ").append(poll.getTitle()).append("\n");
        sb.append("Açıklama: ").append(poll.getDescription()).append("\n");
        sb.append("Sorular ve Oylar:\n");

        poll.getQuestions().forEach(q -> {
            sb.append("- Soru: ").append(q.getText()).append("\n");
            q.getOptions().forEach(o -> {
                sb.append("   - Seçenek: ").append(o.getText())
                        .append(", Oy Sayısı: ").append(o.getVoteCount()).append("\n");
            });
        });

        sb.append("\nBu anketin sonuçlarını analiz et ve kısa, anlaşılır bir yorum yap.");
        return sb.toString();
    }

    private String sendToOpenAI(String prompt) {
        System.out.println("OpenAI API çağrısı yapılıyor.");
        try {
            // İstek gövdesi JSON olarak hazırlanıyor
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");

            List<Map<String, String>> messages = List.of(
                    Map.of(
                            "role", "user",
                            "content", prompt
                    )
            );
            requestBody.put("messages", messages);

            System.out.println("OpenAI için gönderilen istek body’si: " + requestBody.toString());

            // HTTP başlıkları hazırlanıyor
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

            // İstek gönderiliyor
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            System.out.println("OpenAI API’den dönüş durumu: " + response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBodyString = response.getBody();
                System.out.println("OpenAI API cevabı: " + responseBodyString);

                JSONObject responseBody = new JSONObject(responseBodyString);
                String content = responseBody
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");

                System.out.println("OpenAI cevabından çıkarılan içerik: " + content);
                return content;
            } else {
                System.out.println("OpenAI API hata durumu: " + response.getStatusCode() + ", mesaj: " + response.getBody());
                return "Yapay zeka şu anda yanıt veremiyor. Lütfen daha sonra tekrar deneyin.";
            }
        } catch (Exception e) {
            System.out.println("OpenAI API çağrısı sırasında hata oluştu:");
            e.printStackTrace();
            return "Yapay zeka çağrısı sırasında hata oluştu: " + e.getMessage();
        }
    }

}

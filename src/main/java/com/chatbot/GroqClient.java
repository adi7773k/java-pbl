package com.chatbot;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GroqClient {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    public String getChatResponse(String userMessage) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // 👇 Yeh system prompt AI ko instruction dega
        Map<String, Object> systemMessage = Map.of(
            "role", "system",
            "content", "You are a Hinglish AI chatbot. Har message mein desi, friendly aur thoda funny style rakho. Indian user ho toh unke tone mein baat karo. Short, relatable aur helpful answers do."
        );
        Map<String, Object> userMessageMap = Map.of("role", "user", "content", userMessage);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama3-8b-8192");
        body.put("messages", new Object[]{systemMessage, userMessageMap});

        RequestBody requestBody;
        try {
            requestBody = RequestBody.create(
                mapper.writeValueAsString(body),
                MediaType.parse("application/json")
            );
        } catch (IOException e) {
            return "Error creating request body.";
        }

        Request request = new Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer " + API_KEY)
            .post(requestBody)
            .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println("Groq response code: " + response.code());
            System.out.println("Groq response body: " + responseBody);

            if (!response.isSuccessful()) return "Failed to get response.";

            Map<?, ?> json = mapper.readValue(responseBody, Map.class);
            return ((Map<?, ?>)((Map<?, ?>)((java.util.List<?>)json.get("choices")).get(0)).get("message")).get("content").toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error during API call.";
        }
    }
}

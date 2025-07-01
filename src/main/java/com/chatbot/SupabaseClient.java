package com.chatbot;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SupabaseClient {
    private static final String SUPABASE_URL = "https://xuqlymiguqdoxcsahgbx.supabase.co";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh1cWx5bWlndXFkb3hjc2FoZ2J4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDM4Mjk0MTIsImV4cCI6MjA1OTQwNTQxMn0.yzXaINOmrKlwREj-4bzKTpTFwhYmmXWpu3xs1ldQJHA";
    private static final String TABLE = "messages";

    public void saveMessage(String userId, String message, String responseText) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> data = new HashMap<>();
        data.put("user_id", userId);
        data.put("message", message);
        data.put("response", responseText);

        RequestBody body;
        try {
            body = RequestBody.create(mapper.writeValueAsString(data), MediaType.parse("application/json"));
        } catch (IOException e) {
            System.out.println("Error creating JSON body.");
            return;
        }

        Request request = new Request.Builder()
            .url(SUPABASE_URL + "/rest/v1/" + TABLE)
            .addHeader("apikey", API_KEY)
            .addHeader("Authorization", "Bearer " + API_KEY)
            .addHeader("Content-Type", "application/json")
            .addHeader("Prefer", "return=minimal")
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Failed to save to Supabase: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println("Error while saving to Supabase.");
        }
    }
}

package com.chatbot;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GroqClient groqClient = new GroqClient(); // Replace OpenAIClient with GroqClient
        SupabaseClient supabase = new SupabaseClient();

        System.out.println("Human-AI Chatbot. Type 'exit' to quit.");
        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            String response = groqClient.getChatResponse(input); // Use GroqClient here
            System.out.println("AI: " + response);

            supabase.saveMessage("user1", input, response);
        }

        scanner.close();
    }
}


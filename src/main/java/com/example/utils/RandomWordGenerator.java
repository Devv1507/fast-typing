package com.example.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.interfaces.IWordGenerator;
import com.google.gson.Gson;

/**
 * Utility class to generate random words using the Random Word API.
 */
public class RandomWordGenerator implements IWordGenerator {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiUrl = "https://random-word-api.herokuapp.com/word?lang=es"; // Adjust as needed

    /**
     * Generates a random word from an external API.
     *
     * @return A randomly selected word.
     * @throws IOException If an I/O error occurs when sending or receiving the request.
     * @throws InterruptedException If the operation is interrupted.
     */
    @Override
    public String generateWord() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                String[] words = gson.fromJson(response.body(), String[].class);
                if (words != null && words.length > 0) {
                    return words[0];
                } else {
                    // Handle case where the API returns an empty array
                    return "defaultWord"; // Or throw an exception
                }
            } else {
                // Handle error codes from the API
                System.err.println("API returned status code: " + response.statusCode());
                return "defaultWord"; // Or throw an exception
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching word from API: " + e.getMessage());
            throw new RuntimeException("Failed to fetch word from API", e);
        }
    }
}
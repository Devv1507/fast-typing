package com.example.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.interfaces.IWordGenerator;
import com.google.gson.Gson;

/**
 * Clase de utilidad para generar palabras aleatorias utilizando la API Random Word.
 */
public class RandomWordGenerator implements IWordGenerator {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiUrl = "https://random-word-api.herokuapp.com/word?lang=es"; // Ajustar según sea necesario

    /**
     * Genera una palabra aleatoria desde una API externa.
     * @return Una palabra seleccionada aleatoriamente.
     * @throws IOException Si ocurre un error de E/S al enviar o recibir la solicitud.
     * @throws InterruptedException Si la operación es interrumpida.
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
                    return "palabra predeterminada";
                }
            } else {
                return "palabra predeterminada";
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("No se pudo obtener la palabra de la API", e);
        }
    }
}

//package ru.yandex.practicum.filmorate.controller;
//
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class UserControllerTest {
//
//    private final HttpClient client = HttpClient.newHttpClient();
//
//    @Test
//    void shouldAddUser() {
//        String body = "{\n" +
//                "  \"login\": \"dolore\",\n" +
//                "  \"name\": \"Nick Name\",\n" +
//                "  \"email\": \"mail@mail.ru\",\n" +
//                "  \"birthday\": \"1946-08-20\"\n" +
//                "}";
//        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://localhost:8080/users"))
//                .header("Content-Type", "application/json")
//                .header("Accept", "*/*")
//                .POST(HttpRequest.BodyPublishers.ofString(body))
//                .build();
//        try {
//            HttpResponse<String> addResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//            assertEquals(200, addResponse.statusCode());
//            addResponse.body()
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
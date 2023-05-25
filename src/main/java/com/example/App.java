package com.example;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class App {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {

        HttpEntity<String> request = new HttpEntity<>(null);

        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);

        HttpHeaders responseHeaders = response.getHeaders();

        List<String> cookies = responseHeaders.get("set-cookie");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
        System.out.println("Хэдеры запроса: " + headers);
        System.out.println("---------------------------------------------------");

        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte) 50);

        HttpEntity<User> request1 = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> responseEntity1 = restTemplate.postForEntity(URL, request1, String.class);
        String firstPartOfCode = responseEntity1.getBody();

        User updatedUser = newUser;
        updatedUser.setName("Thomas");
        updatedUser.setLastName("Shelby");

        HttpEntity<User> request2 = new HttpEntity<>(updatedUser, headers);
        ResponseEntity<String> responseEntity2 = restTemplate.exchange(URL, HttpMethod.PUT, request2, String.class);
        String secondPartOfCode = responseEntity2.getBody();

        HttpEntity<User> request3 = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity3 = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, request3, String.class);
        String thirdPartOfCode = responseEntity3.getBody();
        System.out.println("Код: " + firstPartOfCode + secondPartOfCode + thirdPartOfCode);

        System.out.println("---------------------------------------------------");

    }
}

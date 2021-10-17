package com.example.heroku;

import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
public class AlphaVantageWebClient {

    public String tickerInformation() {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=demo";

        WebClient client = WebClient.create("https://www.alphavantage.co");
        //WebClient.UriSpec<WebClient.ResponseSpec> uriSpec = client.post();

        ResponseEntity<String> response = client.get()
                .uri("/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=demo")
                .retrieve()
                .toEntity(String.class)
                .block();
        System.out.println(response);
        return String.valueOf(response);
    }
}

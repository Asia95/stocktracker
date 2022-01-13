package com.stocktracker;

import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import static com.stocktracker.util.Constants.ALPHA_VANTAGE_URL;

@NoArgsConstructor
public class AlphaVantageWebClient {

    public String tickerInformation() {
        String dailyUrl = ALPHA_VANTAGE_URL + "/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=demo";

        WebClient client = WebClient.create(ALPHA_VANTAGE_URL);
        //WebClient.UriSpec<WebClient.ResponseSpec> uriSpec = client.post();

        ResponseEntity<String> response = client.get()
                .uri(dailyUrl)
                .retrieve()
                .toEntity(String.class)
                .block();
        System.out.println(response);
        return String.valueOf(response);
    }
}

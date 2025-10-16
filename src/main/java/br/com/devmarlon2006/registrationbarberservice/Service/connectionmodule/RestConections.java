package br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestConections<T> {

    private final RestTemplate restTemplate;

    public RestConections() {
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<?> GetConection(String url , T Object){
        return restTemplate.postForObject( url, Object, ResponseEntity.class );
    }

}

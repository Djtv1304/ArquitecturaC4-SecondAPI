package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Map;
import java.util.HashMap;

@RestController
public class Controller {

    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    @GetMapping("/obtener-puntos/{identificacion}")
    public Map<String, Object> obtenerPuntos(@PathVariable String identificacion) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://consultaweb.ant.gob.ec/PortalWEB/paginas/clientes/clp_grid_citaciones.jsp" +
                "?ps_tipo_identificacion=CED&ps_identificacion=" + identificacion;

        String response = null;
        Map<String, Object> responseJson = new HashMap<>();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            response = responseEntity.getBody();

            // Usar Jsoup para parsear el HTML
            Document doc = Jsoup.parse(response);
            Element puntosElement = doc.select("td:contains(Puntos:) + td").first();

            if (puntosElement != null) {
                String puntos = puntosElement.text();
                System.out.println("Puntos: " + puntos);
                responseJson.put("puntos", puntos);
            } else {
                responseJson.put("error", "No se pudo encontrar el elemento de puntos en el HTML.");
            }
        } catch (HttpClientErrorException e) {
            responseJson.put("error", e.getResponseBodyAsString());
        } catch (Exception e) {
            responseJson.put("error", e.getMessage());
        }

        return responseJson;
    }
}

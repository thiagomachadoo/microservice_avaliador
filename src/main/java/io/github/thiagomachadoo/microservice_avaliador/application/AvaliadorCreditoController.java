package io.github.thiagomachadoo.microservice_avaliador.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("avaliacoes")
public class AvaliadorCreditoController {

    @GetMapping
    public String status(){
        return "OK!!";
    }
}

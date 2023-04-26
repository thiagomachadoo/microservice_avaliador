package io.github.thiagomachadoo.microservice_avaliador.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Cartao {
    private Long id;
    private String nome;
    private String bandeira;
    private BigDecimal limiteBasico;
}

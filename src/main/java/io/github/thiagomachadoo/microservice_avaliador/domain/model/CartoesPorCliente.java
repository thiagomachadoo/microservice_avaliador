package io.github.thiagomachadoo.microservice_avaliador.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartoesPorCliente {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;
}

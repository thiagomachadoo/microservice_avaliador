package io.github.thiagomachadoo.microservice_avaliador.domain.model;

import lombok.Data;

@Data
public class DadosCliente {

    private Long id;
    private String nome;
    private Integer idade;
}

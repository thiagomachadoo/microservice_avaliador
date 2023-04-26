package io.github.thiagomachadoo.microservice_avaliador.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseAvaliacaoCliente {

    private List<CartaoAprovado> cartaoAprovados;
}

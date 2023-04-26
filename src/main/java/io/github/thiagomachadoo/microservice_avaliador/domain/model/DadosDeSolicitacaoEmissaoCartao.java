package io.github.thiagomachadoo.microservice_avaliador.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DadosDeSolicitacaoEmissaoCartao {

    private Long idCartao;
    private String cpf;
    private String endereco;
    private BigDecimal limiteLiberado;
}


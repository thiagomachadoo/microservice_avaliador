package io.github.thiagomachadoo.microservice_avaliador.application;

import io.github.thiagomachadoo.microservice_avaliador.domain.model.DadosCliente;
import io.github.thiagomachadoo.microservice_avaliador.domain.model.SituacaoCliente;
import io.github.thiagomachadoo.microservice_avaliador.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;


    public SituacaoCliente obterSituacaoCliente(String cpf){
        ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponse.getBody())
                .build();
    }
}

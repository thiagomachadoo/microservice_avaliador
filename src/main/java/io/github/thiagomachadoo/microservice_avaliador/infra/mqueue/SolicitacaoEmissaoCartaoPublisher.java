package io.github.thiagomachadoo.microservice_avaliador.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thiagomachadoo.microservice_avaliador.domain.model.DadosDeSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissaoCartoes;

    public void solicitarCartao(DadosDeSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        var json = converteIntoJSON(dados);
        rabbitTemplate.convertAndSend(queueEmissaoCartoes.getName(), json);
    }

    private String converteIntoJSON(DadosDeSolicitacaoEmissaoCartao dados) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(dados);
        return json;
    }
}

package io.github.thiagomachadoo.microservice_avaliador.application;

import feign.FeignException;
import io.github.thiagomachadoo.microservice_avaliador.application.exeption.DadosClienteNotFoundException;
import io.github.thiagomachadoo.microservice_avaliador.application.exeption.ErroComunicacaoMicroservicesException;
import io.github.thiagomachadoo.microservice_avaliador.application.exeption.ErroSolicitacaoCartaoException;
import io.github.thiagomachadoo.microservice_avaliador.domain.model.*;
import io.github.thiagomachadoo.microservice_avaliador.infra.clients.CartoesResourceClient;
import io.github.thiagomachadoo.microservice_avaliador.infra.clients.ClienteResourceClient;
import io.github.thiagomachadoo.microservice_avaliador.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesPorCliente;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;


    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<CartoesPorCliente>> cartoesResponse = cartoesPorCliente.getCartoesByCliente(cpf);
            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public ResponseAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws ErroComunicacaoMicroservicesException, DadosClienteNotFoundException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartaoResponse = cartoesPorCliente.getCartoesRendaAte(renda);

            List<Cartao> cartoes = cartaoResponse.getBody();

            var listacartoesAprovados = cartoes.stream().map(cartao -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBigDecimal = BigDecimal.valueOf(dadosCliente.getIdade());

                var fator = idadeBigDecimal.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);


                CartaoAprovado cartaoAprovado = new CartaoAprovado();
                cartaoAprovado.setCartao(cartao.getNome());
                cartaoAprovado.setBandeira(cartao.getBandeira());
                cartaoAprovado.setLimiteAprovado(limiteAprovado);

                return cartaoAprovado;
            }).collect(Collectors.toList());

            return new ResponseAvaliacaoCliente(listacartoesAprovados);
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoDeCartao(DadosDeSolicitacaoEmissaoCartao dados) {
        try {
            emissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}

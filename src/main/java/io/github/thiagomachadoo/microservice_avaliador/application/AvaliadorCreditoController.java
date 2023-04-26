package io.github.thiagomachadoo.microservice_avaliador.application;

import io.github.thiagomachadoo.microservice_avaliador.application.exeption.DadosClienteNotFoundException;
import io.github.thiagomachadoo.microservice_avaliador.application.exeption.ErroComunicacaoMicroservicesException;
import io.github.thiagomachadoo.microservice_avaliador.application.exeption.ErroSolicitacaoCartaoException;
import io.github.thiagomachadoo.microservice_avaliador.domain.model.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping("avaliacoes")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status(){
        return "OK!!";
    }
    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf){
        SituacaoCliente situacaoCliente = null;
        try {
            situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados){
        try {
          ResponseAvaliacaoCliente responseAvaliacaoCliente = avaliadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
          return ResponseEntity.ok(responseAvaliacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping("solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosDeSolicitacaoEmissaoCartao dados){
        try{
            ProtocoloSolicitacaoCartao protocolo = avaliadorCreditoService.solicitarEmissaoDeCartao(dados);
            return ResponseEntity.ok(protocolo);
        }catch (ErroSolicitacaoCartaoException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}

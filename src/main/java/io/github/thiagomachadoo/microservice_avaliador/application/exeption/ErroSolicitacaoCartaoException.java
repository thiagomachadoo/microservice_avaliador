package io.github.thiagomachadoo.microservice_avaliador.application.exeption;

public class ErroSolicitacaoCartaoException extends RuntimeException{
    public ErroSolicitacaoCartaoException(String message){
        super(message);
    }
}

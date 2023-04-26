package io.github.thiagomachadoo.microservice_avaliador.application.exeption;

public class DadosClienteNotFoundException extends Exception{
    public DadosClienteNotFoundException(){
        super("Dados do cliente não encontrado na base de dados!");
    }
}

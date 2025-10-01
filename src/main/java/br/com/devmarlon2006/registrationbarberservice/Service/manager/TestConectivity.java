/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.manager;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.myexeptions.ConnectionDestroyed;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;

@Service
public class TestConectivity {

    private static final String EXPECTED_OK = "OK" ;
    private final DataSource dataSource;

    /**
     * Payload simples para envio de log de tentativa de conexão a um endpoint externo.
     * Contém um timestamp da tentativa e uma mensagem estática de teste.
     */
    static class ConectionLog{
        public Timestamp timestamp;
        public final String message = "Teste";
    }

    private final RestTemplate restTemplate;

    /**
     * Construtor do serviço de teste de conectividade.
     *
     * Responsável por injetar a fonte de dados para teste de conexão com o banco
     * e instanciar o {@link RestTemplate} usado para testes de conexão com serviços HTTP externos.
     *
     * @param dataSource fonte de dados configurada para o aplicativo
     */
    public TestConectivity (DataSource dataSource) {
        this.restTemplate = new RestTemplate();
        this.dataSource = dataSource;
    }

    /**
     * Retorna o estado de sucesso para padronização de respostas do serviço.
     *
     * @return {@link ResponseMessages#SUCCESS}
     */
    public ResponseMessages retrieveSuccess(){
        return ResponseMessages.SUCCESS;
    }

    /**
     * Retorna o estado de aviso para padronização de respostas do serviço.
     *
     * @return {@link ResponseMessages#WARNING}
     */
    public ResponseMessages retrieveWarning(){
        return ResponseMessages.WARNING;
    }

    /**
     * Retorna o estado de erro para padronização de respostas do serviço.
     *
     * @return {@link ResponseMessages#ERROR}
     */
    public ResponseMessages retrieveError(){
        return ResponseMessages.ERROR;
    }

    /**
     * Retorna o estado informativo para padronização de respostas do serviço.
     *
     * @return {@link ResponseMessages#INFO}
     */
    public ResponseMessages retrieveInfo(){
        return ResponseMessages.INFO;
    }


    /**
     * Realiza um teste de conectividade com um serviço HTTP externo.
     *
     * Comportamento:
     * - Envia uma requisição POST para a URL informada contendo um {@link ConectionLog}.
     * - Espera como retorno a string literal "OK".
     *
     * Retorno:
     * - {@link ResponseMessages#SUCCESS} quando a resposta é exatamente "OK".
     * - {@link ResponseMessages#WARNING} quando a resposta é diferente de "OK" ou está ausente.
     *
     * Observações:
     * - Este método não trata explicitamente exceções de rede do {@link RestTemplate} (ex.: timeouts, 4xx/5xx);
     *   tais exceções podem propagar e devem ser tratadas pelo chamador, se necessário.
     *
     * @param url endpoint a ser testado
     * @return {@link ResponseMessages#SUCCESS} quando houver resposta "OK"; {@link ResponseMessages#WARNING} caso contrário
     */
    public ResponseMessages TestConection(String url){
        ConectionLog log = new ConectionLog();
        log.timestamp = new Timestamp(System.currentTimeMillis());
        try{
            final String response = restTemplate.postForObject(url, log, String.class);
            return EXPECTED_OK.equals(response) ? retrieveSuccess() : retrieveWarning();
        }catch (RestClientException e){
            return retrieveWarning();
        }
    }


    /**
     * Testa a conectividade com o banco de dados obtendo uma conexão do {@link DataSource}.
     *
     * Comportamento:
     * - Tenta abrir uma conexão (try-with-resources) e verifica se a conexão obtida não é nula.
     *
     * Retorno:
     * - {@link ResponseMessages#SUCCESS} quando a conexão é obtida com sucesso.
     * - {@link ResponseMessages#WARNING} quando não há exceção, mas a conexão é nula (situação improvável).
     *
     * Exceções:
     * - Em caso de falha ao obter conexão (ex.: indisponibilidade do banco, credenciais inválidas),
     *   lança {@link ConnectionDestroyed} contendo a mensagem original de {@link SQLException}.
     *
     * @return {@link ResponseMessages#SUCCESS} se a conexão for estabelecida; {@link ResponseMessages#WARNING} caso contrário
     * @throws ConnectionDestroyed quando ocorre erro de SQL ao tentar conectar ao banco
     */
    public ResponseMessages TestConectionData() {
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionDestroyed( e.getMessage() );
        }
        return retrieveSuccess();
    }
}

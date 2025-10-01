/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Controllers;

import br.com.devmarlon2006.registrationbarberservice.Service.run.Execute;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.myexeptions.ConnectionDestroyed;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsável pela gestão de operações relacionadas a clientes no sistema.
 * 
 * <p>Este controller expõe endpoints HTTP para o cadastro e processamento de informações de clientes,
 * atuando como camada de entrada da aplicação e orquestrando validações de conectividade antes de
 * delegar operações de negócio aos serviços apropriados.</p>
 * 
 * <p><b>Mapeamento Base:</b> {@code /DataSave}</p>
 * 
 * <p><b>Responsabilidades:</b></p>
 * <ul>
 *   <li>Receber requisições HTTP de cadastro de clientes</li>
 *   <li>Validar conectividade com banco de dados antes de processar requisições</li>
 *   <li>Validar conectividade com APIs externas de registro</li>
 *   <li>Delegar processamento de negócio ao serviço {@link Execute}</li>
 *   <li>Retornar respostas HTTP adequadas conforme resultado das operações</li>
 * </ul>
 * 
 * @author devmarlon2006
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("DataSave")
public class ClientController {


    @Value( "${registration-api.configuration.user-register-url}")
    private String API_URL;
    private final TestConectivity testConectivity;
    private final Execute sever;

    public ClientController(TestConectivity testConectivity, Execute sever) {
        this.testConectivity = testConectivity;
        this.sever = sever;
    }


    @PostMapping("/client")
    public ResponseEntity<?> SavClient(@NonNull @RequestBody Client client){

        // 1. VALIDAÇÃO DE CONECTIVIDADE COM BANCO DE DADOS
        // Testa se o banco de dados está acessível antes de prosseguir com qualquer operação.
        // Essa verificação preventiva evita processamento desnecessário caso o banco esteja indisponível.
        try{
            testConectivity.TestConectionData();
        }catch (ConnectionDestroyed e){
            // Se houver falha na conectividade com o banco, retorna imediatamente com erro 400
            // e uma mensagem descritiva encapsulada em MessageContainer para o cliente da API.
            return ResponseEntity.status( 400 ).body( new MessageContainer<>(e.getMessage()));
        }

        // 2. VALIDAÇÃO DE CONECTIVIDADE COM API EXTERNA
        // Verifica se a API externa de registro de usuários está disponível e respondendo corretamente.
        // WARNING indica que a API não está acessível ou não retornou a resposta esperada ("OK").
        if(testConectivity.TestConection( API_URL ) == ResponseMessages.WARNING){
            // Retorna 401 Unauthorized para indicar que o serviço externo necessário não está disponível.
            return ResponseEntity.status( 503 ).body( HttpStatus.SERVICE_UNAVAILABLE );
        }

        // 3. PROCESSAMENTO DO REGISTRO DO CLIENTE
        // Com todas as validações de infraestrutura aprovadas, delega o processamento da lógica
        // de negócio para o serviço Execute, que realiza validações de dados, verificações de
        // duplicidade e persistência do cliente.
        sever.ProcessClientRegistration( client );

        // 4. RESPOSTA DE SUCESSO
        // Retorna HTTP 200 indicando que o processamento foi concluído com sucesso.
        // Nota: Seria interessante retornar um corpo com informações do cliente criado (ex: ID, timestamp).
        return ResponseEntity.status( 200 ).body( HttpStatus.OK );
    }
}

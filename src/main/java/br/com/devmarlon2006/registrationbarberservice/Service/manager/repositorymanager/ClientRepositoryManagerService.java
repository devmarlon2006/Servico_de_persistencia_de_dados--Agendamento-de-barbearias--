/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager;



import br.com.devmarlon2006.registrationbarberservice.Repository.ClientRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.SuperRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientRepositoryManagerService implements SuperRepositoryManager<Client, String> {

    private final ClientRepository clientRepository;
    private final TestConectivity testConectivity;


    public ClientRepositoryManagerService(ClientRepository clientRepository, TestConectivity testConectivity) {
        this.clientRepository = clientRepository;
        this.testConectivity = testConectivity;
    }

    /**
     * Tenta persistir um registro de {@link Client} aplicando validações prévias e verificações de conflito.
     *
     * Regras de negócio:
     * - Se o registro for nulo, é considerado inexistente e o fluxo retorna {@code ERROR}.
     * - Antes de salvar, verifica conflitos com {@link #repositoryGET(Client, TypeOfReturn)}:
     *   - Se houver conflito (cliente já existente por id/email/username), retorna {@code ERROR}.
     * - Se os atributos tiverem formato válido ({@link #validateAtribiutesFormat(Client)}) e o objeto for
     *   reconhecido como instância de {@link Client} ({@link #isInstance(Class)}), o registro é salvo.
     *   Caso contrário, o objeto é "limpo" via {@link Validation#ClearObject(Object)} (sem efeito colateral externo) e segue com SUCCESS.
     *
     * Observações:
     * - Em caso de exceções de fluxo (objeto inexistente ou conflito de dados), retorna {@code ERROR}.
     * - Na ausência de erros/avisos, retorna {@code SUCCESS}.
     *
     * Efeitos colaterais:
     * - Pode executar {@code clientRepository.save(ClientRecord)} quando as validações forem aprovadas.
     *
     * @param ClientRecord registro do cliente a ser validado e persistido
     * @return {@link ResponseMessages#SUCCESS} em caso de êxito; {@link ResponseMessages#ERROR} quando houver
     *         objeto nulo, conflito detectado ou exceção esperada no fluxo de validação.
     */
    @Override
    public MesagerComplements<String> postOnRepository(Client ClientRecord) {
        MesagerComplements<String> message = new MesagerComplements<>();

        try {
            if (repositoryGET( ClientRecord , TypeOfReturn.NEGATIVE ).equals( ResponseMessages.WARNING )) {
                message.setStatus( ResponseMessages.ERROR );
                message.setMessage("Erro interno - ID erro: cl10");
                return message;
            }
        } catch (NullPointerException e) {
            message.setStatus( ResponseMessages.ERROR );
            message.setMessage("Erro interno - ID Erro: cl11");
            return message;
        }

        if(validateAtribiutesFormat( ClientRecord )) {

            clientRepository.save( ClientRecord );
            message.setStatus( ResponseMessages.SUCCESS );
            message.setMessage("Registro persistido com sucesso - ID Success: cl12");

        }else {
            Validation.ClearObject( ClientRecord );
            message.setStatus( ResponseMessages.ERROR );
            message.setMessage("Erro interno ao tentar persistir o registro - ID erro: cl13");
            return message;
        }

        return message;
    }

    /**
     * Verifica a existência de um {@link Client} no repositório com base em múltiplos campos (id, email, username).
     *
     * Interpretação do retorno:
     * - {@link ResponseMessages#ERROR}: existe um registro com mesmo id, email ou username (conflito).
     * - {@link ResponseMessages#SUCCESS}: não há registro conflitante (livre para persistir).
     *
     * Nota:
     * - Este método não lança exceções; apenas sinaliza o estado por meio de {@link ResponseMessages}.
     *
     * @param ClientRecord cliente de referência para checagem de existência
     * @return {@link ResponseMessages#ERROR} se já existir; {@link ResponseMessages#SUCCESS} caso contrário
     */
    @Override
    public ResponseMessages repositoryGET(Client ClientRecord, TypeOfReturn typeOfReturn) {
        if (clientRepository.existsById( ClientRecord.getId() ) || clientRepository.existsByEmail( ClientRecord.getEmail()) ||
                clientRepository.existsByUsername( ClientRecord.getUsername() )) {

            return switch (typeOfReturn){
                case POSITIVE -> ResponseMessages.SUCCESS;
                case NEGATIVE -> ResponseMessages.WARNING;
            };
        }
        return testConectivity.retrieveInfo(); //Deafull message
    }


    /**
     * Valida o formato dos principais atributos de {@link Client}:
     * - Nome: apenas letras e espaços ({@link Validation#NameIsCorrect(String)})
     * - Email: padrão de email válido ({@link Validation#EmailIsCorrect(String)})
     * - Senha: força mínima (número, minúscula, maiúscula, caractere especial, sem espaços, min. 8) ({@link Validation#PasswordIsCorrect(String)})
     * - ID: composto apenas por dígitos ({@link Validation#MatchCharacter(String)})
     *
     * @param client cliente a ser validado
     * @return true se todos os atributos passarem nas validações; false caso contrário
     */
    public boolean validateAtribiutesFormat(Client client) {
        List<Boolean> list = new ArrayList<>();

        list.add( Validation.NameIsCorrect( client.getName() ) || Validation.MatchCharacter( client.getName() ) );
        list.add( Validation.EmailIsCorrect( client.getEmail() ) || Validation.MatchCharacter( client.getEmail() ) );
        list.add( Validation.PasswordIsCorrect( client.getPassword() ) || Validation.MatchCharacter( client.getPassword()) );
        list.add( Validation.MatchCharacter( client.getId()));

        return list.stream().allMatch( Boolean::booleanValue );
    }
    
    /**
     * Verifica se o objeto informado é uma instância de {@link Client}.
     *
     * @param obj objeto a ser inspecionado
     * @return true se for instância de Client; false caso contrário
     */

    @Override
    public boolean isInstance(Class<?> obj){
        return obj.isAssignableFrom( Client.class);
    }
}

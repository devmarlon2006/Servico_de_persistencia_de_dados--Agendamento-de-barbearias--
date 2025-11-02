package br.com.devmarlon2006.registrationbarberservice.model.client.clientdtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) para registro de novos clientes.
 * 
 * <p>Esta classe é utilizada para transportar os dados de cadastro de clientes
 * entre as camadas da aplicação.</p>
 * 
 * <p><strong>Nota:</strong> Esta classe não possui campo 'id' pois o identificador único
 * será gerado automaticamente por outro serviço responsável pela persistência.</p>
 */

public record ClientRegistrationDTO (

        String username,
        String name,
        String email,
        String hairtype,
        String password,
        String country,
        String state,
        String city,
        Integer age

){}

/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.model.client;

import br.com.devmarlon2006.registrationbarberservice.model.client.clientdtos.ClientRegistrationDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "clients_data")
public class Client {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "hairtype")
    private String hairtype;

    @Column(name = "password")
    private String password;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ClientRole role;

    @Column(name = "age")
    private Integer age;

    public void generateId(){
        this.id = UUID.randomUUID().toString();
    }

    public static Client of() {
        return new Client();
    }


    public Client transformToEntity(ClientRegistrationDTO clientDTO) {
        Client clientRecord = new Client();

        try{

            clientRecord.setAge( clientDTO.getAge() );clientRecord.setName( clientDTO.getName() );clientRecord.setState( clientDTO.getState() );
            clientRecord.setCity(clientDTO.getCity());clientRecord.setHairtype( clientDTO.getHairtype() );clientRecord.setEmail( clientDTO.getEmail() );
            clientRecord.setPassword( clientDTO.getPassword() );

        }catch (NullPointerException e){
            return null;
        }

        return clientRecord;
    }
}

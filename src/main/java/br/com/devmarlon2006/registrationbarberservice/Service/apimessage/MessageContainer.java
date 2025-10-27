/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.apimessage;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MessageContainer<T> {
    private String reponse;
    private final Timestamp Data_Da_Operacao = Timestamp.from( Instant.now());
    private List<T> ResponseComplements; // Codigo ou mensagem de erro

    public MessageContainer(String response,T message) {
        this.ResponseComplements = new ArrayList<>();
        this.ResponseComplements.add(message);
        this.reponse = response;
    }

    public MessageContainer() {
        this.ResponseComplements = new ArrayList<>();
    }

    public void addMessage(T body) {
        if (this.ResponseComplements == null) {
            this.ResponseComplements = new ArrayList<>();
        }
        this.ResponseComplements.add(body);
    }

    public void addList (List<T> list){
        this.ResponseComplements = list;
    }

    public void addResponse(String response){
        this.reponse = response;
    }

    public void removeMessage(){
        if (this.ResponseComplements != null) {
            this.ResponseComplements.clear();
        }
    }
}

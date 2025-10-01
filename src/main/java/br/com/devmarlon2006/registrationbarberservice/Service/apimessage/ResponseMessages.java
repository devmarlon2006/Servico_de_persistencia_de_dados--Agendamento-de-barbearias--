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

public enum ResponseMessages {

    SUCCESS( "Sucesso"),
    ERROR( "Erro"),
    WARNING("Warning"),
    INFO("Info");

    private final String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getResponseMessage(){
        return message;
    }
}

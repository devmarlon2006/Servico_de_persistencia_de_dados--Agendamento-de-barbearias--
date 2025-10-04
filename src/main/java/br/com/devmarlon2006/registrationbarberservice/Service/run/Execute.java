/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class Execute {

    public String FormatLog(String identifier, ResponseMessages status, Class<?> clazz){
        return "SALVAR ENTIDADE " + (clazz.getClass()).getName() +" DE ID:" + identifier + " * STATUS:" + status;
    }


    public List<ResponseMessages> ListResponseMessages(){
        return new ArrayList<>();
    }

    public String[] ArrayLogMessages(int size){
        return new String[size];
    }
}
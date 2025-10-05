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

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.TypeOfReturn;

public interface SuperRepositoryManager<S, T> {
    MesagerComplements<?> postOnRepository(S s);

    ResponseMessages repositoryGET(S s, TypeOfReturn t);

    boolean isInstance(Class<?> t);

    boolean  validateAtribiutesFormat(S s);

}

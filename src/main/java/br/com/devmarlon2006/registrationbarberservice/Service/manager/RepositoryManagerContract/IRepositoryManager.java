/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.manager.RepositoryManagerContract;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.TypeOfReturn;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.ObjectVeryfy;

public interface IRepositoryManager<S> extends ObjectVeryfy<S> {

    // (S) -> Tipo da entidade;

    MesagerComplements postOnRepository(S s);

    ResponseStatus repositoryGET(S s, TypeOfReturn t);

}

package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BaseManagerRepository;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.TypeOfReturn;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.RepositoryManagerContract.IRepositoryManager;

public abstract class BaseRepositoryManager<T> implements IRepositoryManager<T> {

    // (T) -> Tipo dos parametros;

    public abstract MesagerComplements postOnRepository(T t);

    public abstract ResponseStatus repositoryGET(T type , TypeOfReturn t );

    public abstract boolean validateAtributesInputs(T t);

}

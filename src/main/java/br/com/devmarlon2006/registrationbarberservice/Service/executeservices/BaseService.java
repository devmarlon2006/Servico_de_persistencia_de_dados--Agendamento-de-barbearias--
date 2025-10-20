package br.com.devmarlon2006.registrationbarberservice.Service.executeservices;

public abstract class BaseService <T , P> implements IRunService<T , P> {

    @Override
    public abstract T run(P p);

}

package br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions;

public class ObjectNotexist extends RuntimeException {
    public ObjectNotexist(String message) {
        super( message );
    }
}

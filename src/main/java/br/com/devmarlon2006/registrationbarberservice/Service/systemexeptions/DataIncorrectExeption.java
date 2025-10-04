package br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions;

public class DataIncorrectExeption extends RuntimeException {
    public DataIncorrectExeption(String message) {
        super( message );
    }
}

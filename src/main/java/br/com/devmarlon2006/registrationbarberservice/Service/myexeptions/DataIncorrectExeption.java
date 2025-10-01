package br.com.devmarlon2006.registrationbarberservice.Service.myexeptions;

public class DataIncorrectExeption extends RuntimeException {
    public DataIncorrectExeption(String message) {
        super( message );
    }
}

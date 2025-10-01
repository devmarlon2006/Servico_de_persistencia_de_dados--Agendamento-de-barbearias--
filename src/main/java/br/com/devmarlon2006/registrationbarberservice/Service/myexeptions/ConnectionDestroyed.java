package br.com.devmarlon2006.registrationbarberservice.Service.myexeptions;

public class ConnectionDestroyed extends RuntimeException {
    public ConnectionDestroyed(String message) {
        super( message );
    }
}

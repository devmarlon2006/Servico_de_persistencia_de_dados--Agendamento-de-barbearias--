package br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions;

public class ConnectionDestroyed extends RuntimeException {
    public ConnectionDestroyed(String message) {
        super( message );
    }
}

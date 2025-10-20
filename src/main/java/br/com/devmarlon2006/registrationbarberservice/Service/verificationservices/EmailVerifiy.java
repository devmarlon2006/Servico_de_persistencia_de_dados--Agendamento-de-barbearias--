package br.com.devmarlon2006.registrationbarberservice.Service.verificationservices;

public interface EmailVerifiy {

    boolean verifyEmail(String email);

    boolean verifyEmail(String email, String token);

}

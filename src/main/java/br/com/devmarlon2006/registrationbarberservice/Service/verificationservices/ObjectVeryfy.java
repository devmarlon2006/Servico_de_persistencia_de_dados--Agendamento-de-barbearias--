package br.com.devmarlon2006.registrationbarberservice.Service.verificationservices;

public interface ObjectVeryfy <S> {

    default boolean isInstance(Class<?> Classe ,  S obj){
        return obj.getClass().isAssignableFrom(Classe);
    }

    boolean validateAtribiutesFormat(S s);

}

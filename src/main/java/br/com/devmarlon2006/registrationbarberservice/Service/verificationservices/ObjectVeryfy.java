package br.com.devmarlon2006.registrationbarberservice.Service.verificationservices;

public interface ObjectVeryfy <S> {

    /**
     * <div> (S) -> Entity type  </div>
     */

    default boolean isInstance(Class<?> Classe ,  S obj){
        return obj.getClass().isAssignableFrom(Classe);
    }

    boolean validateAtributesInputs(S s);

}

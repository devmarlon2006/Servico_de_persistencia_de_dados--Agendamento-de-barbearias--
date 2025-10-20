/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.verificationservices;


public class Validation  {

    public static boolean MatchCharacter(String value){
        return value.matches("\\d+");
    }


    public static boolean NameIsCorrect(String value){
        String regex = "^[a-zA-Z\\s]+$";
        return value.matches(regex) ;
    }

    public static boolean EmailIsCorrect(String value){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return value.matches(regex);
    }

    public static boolean PhoneIsCorrect(String value){
        String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
        return value.matches(regex);
    }

    public static boolean PasswordIsCorrect(String value) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return value.matches( regex );
    }

}

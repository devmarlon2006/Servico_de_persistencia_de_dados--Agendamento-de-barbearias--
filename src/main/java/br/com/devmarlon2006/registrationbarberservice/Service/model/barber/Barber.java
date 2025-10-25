/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.model.barber;

import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.barberdto.BarberRegistrationDTO;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.barbershopdtos.BarberShopRegistrationDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "barber_data")
public class Barber {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "country")
    private String country;

    @Column(name = "score")
    private Integer score;

    @JsonIgnore
    public void DeafullScore(){
        if (score == null || score < 0) score = 0;
    }

    @JsonIgnore
    public void generateId(){
        this.id = UUID.randomUUID().toString();
    }

    public void tranformEntity(BarberRegistrationDTO barberShopRecord) {
        this.name = barberShopRecord.getName();
        this.email = barberShopRecord.getEmail();
        this.phone = barberShopRecord.getPhone();
        this.password = barberShopRecord.getPassword();
        this.country = barberShopRecord.getCountry();
    }

    public static Barber of () {
        return new Barber();
    }
}

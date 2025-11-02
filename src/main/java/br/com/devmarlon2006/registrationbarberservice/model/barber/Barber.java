/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.model.barber;

import br.com.devmarlon2006.registrationbarberservice.model.barber.barberdto.BarberRegistrationDTO;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.BarberShop;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private BarberRoles role;

    @OneToOne(mappedBy = "ownerId")
    private BarberShop barbershop;

    @JsonIgnore
    public void DeafullScore(){
        if (score == null || score < 0) score = 0;
    }

    public void deafullRole() {

        if (this.role == null) {
            this.role = BarberRoles.BARBEIRO;
        }
    }

    @JsonIgnore
    public void generateId(){
        this.id = UUID.randomUUID().toString();
    }

    public void updateFromRegistration(BarberRegistrationDTO barberShopRecord) {
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

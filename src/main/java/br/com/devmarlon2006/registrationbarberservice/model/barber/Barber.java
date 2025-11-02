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
    public void DeafullScore() {
        if (score == null || score < 0) score = 0;
    }

    public Barber(){}

    public Barber(String name, String email, String phone, String password, String country) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.country = country;
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
        this.name = barberShopRecord.name();
        this.email = barberShopRecord.email();
        this.phone = barberShopRecord.phone();
        this.password = barberShopRecord.password();
        this.country = barberShopRecord.country();
    }

    public static Barber buildFromRegistrationDTO(BarberRegistrationDTO barberDTO) {
        return  new Barber(barberDTO.name(), barberDTO.email(), barberDTO.phone(), barberDTO.password(), barberDTO.country());
    }

    public static Barber of () {
        return new Barber();
    }
}

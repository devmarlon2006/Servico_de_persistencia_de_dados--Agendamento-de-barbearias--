/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Repository;

import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.BarberShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BarberRepository extends JpaRepository<Barber, String> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByBarbershop(BarberShop barbershop);
    Barber findByName(String name);
    Barber findByPhone(String phone);
    Barber findByBarbershop(BarberShop barbershop);
    Barber findByEmail(String email);
}

package br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos;

import br.com.devmarlon2006.registrationbarberservice.model.barber.barberdto.BarberRegistrationDTO;
import lombok.Data;

import java.sql.Time;

/**
 * DTO utilizado para o cadastro completo de uma barbearia junto com seu proprietário.
 * Esta classe agrupa as informações da barbearia e os dados do barbeiro que será
 * registrado como dono do estabelecimento em uma única operação.
 * 
 * @author devmarlon2006
 */

public record BarberShopWithOwnerRegistrationDTO (
        String name ,
        BarberRegistrationDTO ownerId ,
        String phone ,
        String address ,
        Time openTime ,
        Time closeTime ,
        Time holidayTime ,
        String description) {


}

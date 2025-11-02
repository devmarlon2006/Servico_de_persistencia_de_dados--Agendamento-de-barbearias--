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

@Data
public class BarberShopWithOwnerRegistrationDTO {

    private String name;

    private BarberRegistrationDTO ownerId; // Dados completos do proprietário da barbearia

    private String phone;

    private String address;

    private Time openTime;

    private Time closeTime;

    private Time holidayTime;

    private String description;
}

package br.com.devmarlon2006.registrationbarberservice.model.barbershop;

import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos.BarberShopRegistrationDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "barbearias")
@Setter @Getter
public class BarberShop {

    @Id
    @Column(name = "id_barbearia")
    private String id;

    @OneToOne
    @JoinColumn(name = "id_dono")
    private Barber ownerId;

    @Column(name = "nome_barbearia")
    @NotBlank(message = " Campo obrigatorio: nome " )
    private String name;

    @Column(name = "telefone")
    private String phone;

    @Column(name = "endereco")
    private String address;

    @Column(name = "hora_abertura")
    private LocalTime openTime;

    @Column(name = "hora_fechamento")
    private LocalTime closeTime;

    @Column(name = "dia_folga")
    private String holidayTime;  // Mudei de Time para String (parece ser dia da semana)

    @Column(name = "descricao")
    private String description;


    public String getOwerID() {
        if (ownerId == null) throw new NullPointerException("BarberShop ownerId is null") ;
        return ownerId.getId();
    }

    public void generateId(){

        if (this.id  == null){
            this.id = UUID.randomUUID().toString();
        }

    }

    public void transformEntity(BarberShopRegistrationDTO barberShopDTO) {
        this.name = barberShopDTO.getName();
        this.phone = barberShopDTO.getPhone();
        this.address = barberShopDTO.getAddress();
        this.openTime = barberShopDTO.getOpenTime();
        this.closeTime = barberShopDTO.getCloseTime();
        this.holidayTime = barberShopDTO.getHolidayTime();
        this.description = barberShopDTO.getDescription();
    }

    public static BarberShop of () {
        return new BarberShop();
    }

}

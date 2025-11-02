package br.com.devmarlon2006.registrationbarberservice.model.barbershop;

import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos.BarberShopRegistrationDTO;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos.BarberShopWithOwnerRegistrationDTO;
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

    public BarberShop() {
    }

    BarberShop(String name , String phone , String address , LocalTime openTime , LocalTime closeTime , String holidayTime , String description) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.holidayTime = holidayTime;
        this.description = description;
    }




    public String getOwerID() {
        if (ownerId == null) throw new NullPointerException("BarberShop ownerId is null") ;
        return ownerId.getId();
    }

    public void generateId(){

        if (this.id  == null){
            this.id = UUID.randomUUID().toString();
        }

    }

    public static BarberShop buildFromRegistrationDTO(BarberShopRegistrationDTO barberShopDTO) {

        return new BarberShop(

                barberShopDTO.name(),
                barberShopDTO.phone(),
                barberShopDTO.address(),
                barberShopDTO.openTime(),
                barberShopDTO.closeTime(),
                barberShopDTO.holidayTime(),
                barberShopDTO.description()

        );


    }

    public static BarberShop of () {
        return new BarberShop();
    }

}

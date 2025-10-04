package br.com.devmarlon2006.registrationbarberservice.Service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

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
    private String name;

    @Column(name = "telefone")
    private String phone;

    @Column(name = "endereco")
    private String address;

    @Column(name = "hora_abertura")
    private Time openTime;

    @Column(name = "hora_fechamento")
    private Time closeTime;

    @Column(name = "dia_folga")
    private Time holidayTime;

    @Column(name = "descricao")
    private String description;


    public String getOwerID(){
        return ownerId.getId();
    }

}

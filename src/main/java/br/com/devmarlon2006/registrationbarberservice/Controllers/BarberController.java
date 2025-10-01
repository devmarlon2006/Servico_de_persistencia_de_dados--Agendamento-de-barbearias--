/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Controllers;


import br.com.devmarlon2006.registrationbarberservice.Service.manager.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("DataSave")
public class BarberController {

    private final BarberRepositoryManagerService BarberSave;
    public BarberController(BarberRepositoryManagerService BarberSave) {
        this.BarberSave = BarberSave;
    }

    @PostMapping("/Barber")
    public ResponseEntity<?> SavBarber(Barber barber){
        BarberSave.PostOnRepository( barber );
        return ResponseEntity.status( 200 ).body( "saved" );
    }

}

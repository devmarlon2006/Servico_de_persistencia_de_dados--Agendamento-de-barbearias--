package br.com.devmarlon2006.registrationbarberservice.Controllers;

import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.model.DataTransferObject;
import br.com.devmarlon2006.registrationbarberservice.Service.run.Plus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("DataSave")
public class BarberAndShopController {

    private final Plus plus;


    public BarberAndShopController(Plus plus) {
        this.plus = plus;
    }

    @PostMapping
    public ResponseEntity<?> controllerShop(DataTransferObject data) {
        plus.Montagem(data);
        return ResponseEntity.ok("Success");
    }
}

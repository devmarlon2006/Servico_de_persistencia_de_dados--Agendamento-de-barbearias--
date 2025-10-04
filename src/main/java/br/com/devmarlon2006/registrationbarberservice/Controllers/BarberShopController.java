package br.com.devmarlon2006.registrationbarberservice.Controllers;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.run.BarberShopService;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("DataSave")
public class BarberShopController {

    private final BarberShopService barberShopService;
    private final TestConectivity testConectivity;

    public BarberShopController(BarberShopService barberShopService, TestConectivity testConectivity) {
        this.barberShopService = barberShopService;
        this.testConectivity = testConectivity;
    }

    @PostMapping("/BarberShop")
    public ResponseEntity<?> SavBarberShop(@RequestBody BarberShop barberShopRecord){

        try {
            testConectivity.TestConectionData();
        }catch (ConnectionDestroyed e){
            return ResponseEntity.status( 400 ).body( new MessageContainer<>(e.getMessage()));
        }

        MessageContainer<?> message = barberShopService.processBarberShopRegistration( barberShopRecord, barberShopRecord.getOwerID());
        return ResponseEntity.status( 200 ).body( message );
    }
}

package br.com.devmarlon2006.registrationbarberservice.Controllers;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.model.DataTransferObject;
import br.com.devmarlon2006.registrationbarberservice.Service.run.BarberShopService;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> SavBarberShop(@RequestBody DataTransferObject barberShopDTO){

        try {
            testConectivity.TestConectionData();
        }catch (ConnectionDestroyed e){
            return ResponseEntity.status( 400 ).body( new MessageContainer<>(e.getMessage()));
        }

        if (testConectivity.TestConection( "teste" ) == ResponseMessages.WARNING){

            return ResponseEntity.status( 503 ).body( HttpStatus.SERVICE_UNAVAILABLE );

        }

        MessageContainer<?, ?> message = barberShopService.processBarberShopRegistration( barberShopDTO );
        return ResponseEntity.status( 200 ).body( message );
    }
}

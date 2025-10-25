package br.com.devmarlon2006.registrationbarberservice.Controllers;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.barbershopdtos.BarberShopRegistrationDTO;
import br.com.devmarlon2006.registrationbarberservice.Service.applicationservices.BarberShopService;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${endpoints.persistence}")
public class BarberShopController {

    private final BarberShopService barberShopService;
    private final ConnectivityService connectivityService;

    public BarberShopController(BarberShopService barberShopService, ConnectivityService connectivityService) {
        this.barberShopService = barberShopService;
        this.connectivityService = connectivityService;
    }

    @PostMapping("${api.entity's.barberShop}")
    public ResponseEntity<?> SavBarberShop(@RequestBody BarberShopRegistrationDTO barberShopDTO){

        try {
            connectivityService.TestConectionData();
        }catch (ConnectionDestroyed e){
            return ResponseEntity.status( 400 ).body( HttpStatus.BAD_REQUEST);
        }

        try{

            MessageContainer<?> registrationResponse = barberShopService.processBarberShopRegistration( barberShopDTO );

            if (registrationResponse.getReponse().equals("error")){
                return ResponseEntity.status( 400 ).body( registrationResponse );
            }

            return ResponseEntity.status( HttpStatus.CREATED ).body( registrationResponse );
        }catch (Exception e) {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( e.getMessage() );
        }
    }
}

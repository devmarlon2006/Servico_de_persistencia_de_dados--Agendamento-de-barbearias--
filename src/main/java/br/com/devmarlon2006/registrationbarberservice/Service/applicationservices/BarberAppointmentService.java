package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.BarberPlusShopManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.barbershopdtos.BarberShopWithOwnerRegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public class BarberAppointmentService {

    private final BarberPlusShopManager barberPlusShopManager;


    public BarberAppointmentService(BarberPlusShopManager barberPlusShopManager) {
        this.barberPlusShopManager = barberPlusShopManager;
    }

    public MessageContainer<MesagerComplements,String> processAppointment(BarberShopWithOwnerRegistrationDTO data) {

        MessageContainer<MesagerComplements, String> container = new MessageContainer<>();
        MesagerComplements message;

        try{

            message = barberPlusShopManager.processBarberPlusShop(data);
            container.addMesage( message, 0 );

        }catch (NullPointerException e){

            return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                    new MesagerComplements( ResponseStatus.ERROR , OperationStatusCode.ERROR_UNEXPECTED ,
                    OperationStatusCode.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage()) ));

        }

        return container;
    }
}

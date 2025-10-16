package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.StatusOperation;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BarberPlusShopManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.model.DataTransferObject;
import org.springframework.stereotype.Service;

@Service
public class BarberAppointmentService {

    private final BarberPlusShopManager barberPlusShopManager;


    public BarberAppointmentService(BarberPlusShopManager barberPlusShopManager) {
        this.barberPlusShopManager = barberPlusShopManager;
    }

    public MessageContainer<MesagerComplements,String> processAppointment(DataTransferObject data) {

        BarberShop barberShopRecord = (BarberShop) data.requiredType(BarberShop.class ); // Pegar ou objeto do tipo (BarberShop)
        Barber barberRecord = (Barber) data.requiredType(Barber.class); // Pegar ou objeto do tipo (Barber)

        MessageContainer<MesagerComplements, String> container = new MessageContainer<>();
        MesagerComplements message;

        try{

            message = barberPlusShopManager.processBarberPlusShop(barberShopRecord, barberRecord);
            container.addMesage( message, 0 );

        }catch (NullPointerException e){

            return new MessageContainer<>(ResponseMessages.ERROR.getResponseMessage(),
                    new MesagerComplements( ResponseMessages.ERROR , StatusOperation.ERROR_UNEXPECTED ,
                    StatusOperation.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage()) ));

        }

        return container;
    }
}

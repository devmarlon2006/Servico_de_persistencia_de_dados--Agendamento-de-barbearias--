package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.BarberPlusShopManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.barbershopdtos.BarberShopWithOwnerRegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public class BarberAppointmentService {

    private final BarberPlusShopManager barberPlusShopManager;


    public BarberAppointmentService(BarberPlusShopManager barberPlusShopManager) {
        this.barberPlusShopManager = barberPlusShopManager;
    }

    public MessageContainer<MesagerComplements> processAppointment(BarberShopWithOwnerRegistrationDTO barberShopRecord) {

        Barber barber = new Barber();
        barber.tranformEntity( barberShopRecord.getOwnerId() );
        barber.generateId();

        BarberShop barberShop = new BarberShop();
        barberShop.generateId();

        MesagerComplements message;

        try{

            message = barberPlusShopManager.processBarberPlusShop(barber, barberShop);

        }catch (NullPointerException e){

            return new MessageContainer<>( ResponseStatus.ERROR.formatMessage( "Erro inesperado" ),
                    new MesagerComplements( OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro inesperado" )));

        }

        return new MessageContainer<>( ResponseStatus.SUCCESS.formatMessage( "Entidade persistida" ),
                new MesagerComplements( ResponseStatus.SUCCESS.formatMessage( "  " ) ));
    }
}

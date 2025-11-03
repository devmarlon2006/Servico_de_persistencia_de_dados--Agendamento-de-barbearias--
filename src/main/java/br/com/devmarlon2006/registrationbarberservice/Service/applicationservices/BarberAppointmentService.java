package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.modelsassemblersmanagers.BarberAssembler;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.modelsassemblersmanagers.BarberShopAssembler;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.BarberPlusShopManager;
import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos.BarberShopRegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public class BarberAppointmentService {

    private final BarberPlusShopManager barberPlusShopManager;
    private final BarberAssembler barberAssembler;
    private final BarberShopAssembler barberShopAssembler;


    public BarberAppointmentService(BarberPlusShopManager barberPlusShopManager, BarberAssembler barberAssembler, BarberShopAssembler barberShopAssembler) {
        this.barberPlusShopManager = barberPlusShopManager;
        this.barberAssembler = barberAssembler;
        this.barberShopAssembler = barberShopAssembler;
    }

    public MessageContainer<MesagerComplements<String>> processAppointment(BarberShopRegistrationDTO barberShopRecord) {

        Barber barber = barberAssembler.barberAssembler( barberShopRecord.ownerId() ).getBody();

        BarberShop barberShop = barberShopAssembler.barberShopAssemblerNoBarberID( barberShopRecord ).getBody();

        barberShop.generateId();

        MesagerComplements<String> message;

        try{

            message = barberPlusShopManager.processBarberPlusShop(barber, barberShop);

            if (message.getStatus().equals( ResponseStatus.ERROR )) {
                return new MessageContainer<>(ResponseStatus.ERROR.getResponseMessage(), MesagerComplements.complementsOnlyBody( message.getBody() ));
            }

        }catch (NullPointerException e) {

            return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                    MesagerComplements.complementsOnlyBody( OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro inesperado" )));

        }

        return new MessageContainer<>( ResponseStatus.SUCCESS.getResponseMessage(),
                MesagerComplements.complementsOnlyBody( ResponseStatus.SUCCESS.formatMessage( "Entidade persistida" ) ));
    }
}

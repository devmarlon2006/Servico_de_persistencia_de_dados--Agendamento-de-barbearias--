package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.barbershopdtos.BarberShopWithOwnerRegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public class BarberPlusShopManager {

    private final BarberShopRepositoryManager barberShopRepositoryManager;
    private final BarberRepositoryManagerService barberRepositoryManagerService;


    public BarberPlusShopManager(BarberShopRepositoryManager barberShopRepositoryManager, BarberRepositoryManagerService barberRepositoryManagerService) {
        this.barberShopRepositoryManager = barberShopRepositoryManager;
        this.barberRepositoryManagerService = barberRepositoryManagerService;
    }

    public MesagerComplements processBarberPlusShop(BarberShopWithOwnerRegistrationDTO barberShopRecord) {
        Barber barber = new Barber();
        barber.tranformEntity( barberShopRecord.getOwnerId() );
        barber.generateId();

        BarberShop barberShop = new BarberShop();
        barberShop.generateId();
        MesagerComplements Operation = barberRepositoryManagerService.postOnRepository( barber );

        if (Operation.getStatus() == ResponseStatus.ERROR)
        {
            return Operation;
        }

        barber = barberRepositoryManagerService.fyndBarber( barber.getId() );

        if (barber == null) {
            return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_ENTITY_NOT_FOUND);
        }

        try{
            barberShop.setOwnerId( barber );
        }catch (NullPointerException e){
            return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED);
        }

        MesagerComplements operation =
        barberShopRepositoryManager.postOnRepository( barberShop);

        return new MesagerComplements( ResponseStatus.SUCCESS, OperationStatusCode.SUCCESS_ENTITY_CREATED,
                OperationStatusCode.SUCCESS_ENTITY_CREATED.getFormattedMessage(operation.getMessageResponse()) );
    }
}

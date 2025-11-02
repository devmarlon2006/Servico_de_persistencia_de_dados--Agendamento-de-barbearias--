package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.BarberShop;
import org.springframework.stereotype.Service;

@Service
public class BarberPlusShopManager {

    private final BarberShopRepositoryManager barberShopRepositoryManager;
    private final BarberRepositoryManagerService barberRepositoryManagerService;


    public BarberPlusShopManager(BarberShopRepositoryManager barberShopRepositoryManager, BarberRepositoryManagerService barberRepositoryManagerService) {
        this.barberShopRepositoryManager = barberShopRepositoryManager;
        this.barberRepositoryManagerService = barberRepositoryManagerService;
    }

    public MesagerComplements<String> processBarberPlusShop(Barber barber , BarberShop barberShop) {
        MesagerComplements<String> Operation = barberRepositoryManagerService.postOnRepository( barber );

        if (Operation.getStatus() == ResponseStatus.ERROR)
        {
            return Operation;
        }

        barber = barberRepositoryManagerService.fyndBarber( barber.getId() );

        if (barberRepositoryManagerService.existsBarber(barberShop)){
            return MesagerComplements.complementsComplete( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNIQUE_CONSTRAINT.getMessage() );
        }

        if (barber == null) {
            return MesagerComplements.complementsComplete( ResponseStatus.ERROR, OperationStatusCode.ERROR_ENTITY_NOT_FOUND.getMessage());
        }

        try{
            barberShop.setOwnerId( barber );
        }catch (NullPointerException e) {
            return MesagerComplements.complementsComplete( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED.getMessage());
        }

        MesagerComplements <String> operation =
        barberShopRepositoryManager.postOnRepository( barberShop);

        if (operation.getStatus() == ResponseStatus.ERROR) {
            return operation;
        }

        return MesagerComplements.complementsComplete( ResponseStatus.SUCCESS,
                OperationStatusCode.SUCCESS_ENTITY_CREATED.getFormattedMessage(operation.getBody()) );
    }
}

package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.StatusOperation;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import org.springframework.stereotype.Service;

@Service
public class BarberPlusShopManager {

    private final  BarberShopRepositoryManager barberShopRepositoryManager;
    private final BarberRepositoryManagerService barberRepositoryManagerService;


    public BarberPlusShopManager(BarberShopRepositoryManager barberShopRepositoryManager, BarberRepositoryManagerService barberRepositoryManagerService) {
        this.barberShopRepositoryManager = barberShopRepositoryManager;
        this.barberRepositoryManagerService = barberRepositoryManagerService;
    }

    public MesagerComplements processBarberPlusShop(BarberShop barberShopRecord, Barber barberRecord) {

        MesagerComplements Operation = barberRepositoryManagerService.postOnRepository( barberRecord );

        if (Operation.getStatus() == ResponseMessages.ERROR)
        {
            return Operation;
        }

        Barber barber = barberRepositoryManagerService.fyndBarber( barberRecord.getId() );

        if (barber == null) {
            return new MesagerComplements(ResponseMessages.ERROR, StatusOperation.ERROR_ENTITY_NOT_FOUND);
        }


        try{
            barberShopRecord.setOwnerId( barber );
        }catch (NullPointerException e){
            return new MesagerComplements(ResponseMessages.ERROR, StatusOperation.ERROR_UNEXPECTED);
        }

        MesagerComplements message = barberShopRepositoryManager.postOnRepository( barberShopRecord );

        return message;
    }
}

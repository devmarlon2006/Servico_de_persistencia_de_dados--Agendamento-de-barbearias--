package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
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

    public MesagerComplements<String> processBarberPlusShop(BarberShop barberShopRecord, Barber barberRecord) {

        MesagerComplements<String> Operation = barberRepositoryManagerService.postOnRepository( barberRecord );

        if (Operation.getStatus() == ResponseMessages.ERROR)
        {
            return Operation;
        }

        Barber barber = barberRepositoryManagerService.fyndBarber( barberRecord.getId() );

        if (barber == null) {
            return new MesagerComplements<>(ResponseMessages.ERROR, "Fatal Error");
        }

        barberShopRecord.setOwnerId( barber );
        MesagerComplements<String> message = barberShopRepositoryManager.postOnRepository( barberShopRecord );
        message.setMessage( "Barber and Shop saved" );
        return message;
    }
}

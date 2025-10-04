package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BarberShopRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import org.springframework.stereotype.Service;


@Service
public class BarberShopService {

    /*
     * Serviços solicitados
     */
    private final BarberShopRepositoryManager barberShopRepositoryManager;
    private final BarberRepositoryManagerService barberRepositoryManagerService;
    private final Execute execute;


    private final BarberRepository barberRepository;

    public BarberShopService(BarberShopRepositoryManager barberShopRepositoryManager,
                             BarberRepositoryManagerService barberRepositoryManagerService,
                             Execute execute, BarberRepository barberRepository)
    {
        this.barberShopRepositoryManager = barberShopRepositoryManager;
        this.barberRepositoryManagerService = barberRepositoryManagerService;
        this.execute = execute;
        this.barberRepository = barberRepository;
    }


    public MessageContainer<MesagerComplements<?>, String> processBarberShopRegistration(BarberShop barberShopRecord, String barberID){
        MessageContainer<MesagerComplements<?>, String> barberShopMessageContainer = new MessageContainer<>();
        boolean exists = barberRepository.existsById( barberID); //Busca BArber no banco de dados para verificar se existe

        if (exists) {
            barberShopRecord.setOwnerId( barberRepositoryManagerService.fyndBarber( barberID ));
        }else {
            barberShopMessageContainer.addMesage(
                    barberShopMessageContainer.newAresponseComplements( ResponseMessages.WARNING,
                            "Barber not found" ));
            barberShopMessageContainer.addResponse("Error");
            return barberShopMessageContainer;
        }

        try{
            MesagerComplements<?> saveResponse = barberShopRepositoryManager.postOnRepository(barberShopRecord);
            if (saveResponse.getCause().equals(ResponseMessages.SUCCESS))
            {
                barberShopMessageContainer.addMesage(saveResponse);
            }
        }catch (NullPointerException e){
            barberShopMessageContainer.addMesage(barberShopMessageContainer.newAresponseComplements(ResponseMessages.ERROR, "Fatal Error"));
            barberShopMessageContainer.addResponse("Error");
            return barberShopMessageContainer;
        }

        return barberShopMessageContainer;
    }
}

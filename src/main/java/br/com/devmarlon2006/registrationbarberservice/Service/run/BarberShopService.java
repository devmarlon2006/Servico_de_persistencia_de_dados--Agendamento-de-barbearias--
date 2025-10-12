package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BarberShopRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.model.DataTransferObject;
import lombok.NonNull;
import org.springframework.stereotype.Service;


@Service
public class BarberShopService {

    /*
     * Serviços solicitados
     */
    private final BarberShopRepositoryManager barberShopRepositoryManager;
    private final BarberRepositoryManagerService barberRepositoryManagerService;


    private final BarberRepository barberRepository;

    public BarberShopService(BarberShopRepositoryManager barberShopRepositoryManager,
                             BarberRepositoryManagerService barberRepositoryManagerService,
                             BarberRepository barberRepository)
    {
        this.barberShopRepositoryManager = barberShopRepositoryManager;
        this.barberRepositoryManagerService = barberRepositoryManagerService;
        this.barberRepository = barberRepository;
    }


    @NonNull
    public MessageContainer<MesagerComplements<?>, String> processBarberShopRegistration(DataTransferObject barberShopDTO){
        MessageContainer<MesagerComplements<?>, String> barberShopMessageContainer = new MessageContainer<>();
        BarberShop barberShopRecord = (BarberShop) barberShopDTO.requiredType( BarberShop.class);
        String barberID = barberShopRecord.getOwerID();
        boolean exists = barberRepository.existsById( barberID); //Busca BArber no banco de dados para verificar se existe

        if (exists) {

            barberShopMessageContainer.addMesage(
                    barberShopMessageContainer.newAresponseComplements( ResponseMessages.SUCCESS,
                            "Barber found" ), 0 );

            barberShopRecord.setOwnerId( barberRepositoryManagerService.fyndBarber( barberID ));
        }else {
            barberShopMessageContainer.addResponse("Error");

            barberShopMessageContainer.addMesage(
                    barberShopMessageContainer.newAresponseComplements( ResponseMessages.WARNING,
                            "Barber not found" ), 0);
            return barberShopMessageContainer;
        }

        try{
            MesagerComplements<?> saveResponse = barberShopRepositoryManager.postOnRepository(barberShopRecord);
            if (saveResponse.getStatus().equals(ResponseMessages.SUCCESS))
            {
                barberShopMessageContainer.addMesage(saveResponse, 1);
            }
        }catch (NullPointerException e){
            barberShopMessageContainer.addResponse("Error");

            barberShopMessageContainer.addMesage(
                    barberShopMessageContainer.newAresponseComplements(
                            ResponseMessages.ERROR, "Fatal Error"), 0);

            return barberShopMessageContainer;
        }

        return barberShopMessageContainer;
    }
}

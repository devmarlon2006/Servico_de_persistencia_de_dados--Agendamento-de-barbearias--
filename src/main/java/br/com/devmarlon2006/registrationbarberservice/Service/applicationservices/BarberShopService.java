package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.BarberShopRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.barbershopdtos.BarberShopRegistrationDTO;
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
    public MessageContainer<MesagerComplements, String> processBarberShopRegistration(BarberShopRegistrationDTO barberShopDTO) {
        MessageContainer<MesagerComplements, String> barberShopMessageContainer = new MessageContainer<>();

        BarberShop barberShopRecord = new BarberShop();
        barberShopRecord.transformEntity( barberShopDTO );

        boolean exists = barberRepository.existsById( barberShopDTO.getOwerId()); //Busca BArber no banco de dados para verificar se existe

        if (exists) {
            Barber barbe = barberRepository.findById( barberShopDTO.getOwerId()).orElse( null );

            if (barbe == null) {
                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                        new MesagerComplements( ResponseStatus.ERROR , OperationStatusCode.ERROR_ENTITY_NOT_FOUND ));
            }

            barberShopRecord.setOwnerId(barbe);
        }else {
           return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                   new MesagerComplements( ResponseStatus.ERROR , OperationStatusCode.ERROR_ENTITY_NOT_FOUND ));
        }

        try{
            barberShopRecord.generateId();
            MesagerComplements saveResponse = barberShopRepositoryManager.postOnRepository(barberShopRecord);
            if (saveResponse.getStatus().equals( ResponseStatus.SUCCESS))
            {
                barberShopMessageContainer.addResponse( ResponseStatus.SUCCESS.getResponseMessage() );
                barberShopMessageContainer.addMesage(saveResponse, 0);
            }
        }catch (NullPointerException e) {
            return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                    new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED ) );
        }

        return barberShopMessageContainer;
    }
}

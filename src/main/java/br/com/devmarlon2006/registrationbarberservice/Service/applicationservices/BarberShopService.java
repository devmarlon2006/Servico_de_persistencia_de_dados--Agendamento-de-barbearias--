package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.Repository.BarberShopRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
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
    private final BarberShopRepository barberShopRepository;


    private final BarberRepository barberRepository;

    public BarberShopService(BarberShopRepositoryManager barberShopRepositoryManager,
                             BarberShopRepository barberShopRepository, BarberRepository barberRepository)
    {
        this.barberShopRepositoryManager = barberShopRepositoryManager;
        this.barberShopRepository = barberShopRepository;
        this.barberRepository = barberRepository;
    }


    @NonNull
    public MessageContainer<MesagerComplements<String>> processBarberShopRegistration(BarberShopRegistrationDTO barberShopDTO) {

        BarberShop barberShopRecord = BarberShop.of();

        boolean exists = barberRepository.existsById( barberShopDTO.getOwerId()); //Busca BArber no banco de dados para verificar se existe

        if (exists) {

            Barber barbe = barberRepository.findById( barberShopDTO.getOwerId()).orElse( null );


            if (barbe != null) {

                if (barberShopRepository.existsByOwnerId( barbe )) {

                    return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                             MesagerComplements.complementsOnlyBody( OperationStatusCode.ERROR_UNIQUE_CONSTRAINT.getMessage() ));

                }
                barberShopRecord.setOwnerId(barbe);
            }

            if (barbe == null) {

                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                        MesagerComplements.complementsOnlyBody( OperationStatusCode.ERROR_ENTITY_NOT_FOUND.getMessage() ) );
            }

        }else {

           return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                   MesagerComplements.complementsOnlyBody( OperationStatusCode.ERROR_ENTITY_NOT_FOUND.getMessage() ));

        }

        try{

            barberShopRecord.generateId();
            MesagerComplements<String> saveResponse = barberShopRepositoryManager.postOnRepository(barberShopRecord);

            if (saveResponse.getStatus().equals( ResponseStatus.ERROR )) {
                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), MesagerComplements.complementsOnlyBody( saveResponse.getBody() ));
            }

        }catch (NullPointerException e) {

            return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                    MesagerComplements.complementsOnlyBody(
                            OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ) ));

        }

        return new MessageContainer<>(ResponseStatus.SUCCESS.getResponseMessage() ,
                MesagerComplements.complementsOnlyBody(
                        OperationStatusCode.SUCCESS_ENTITY_CREATED.getFormattedMessage( "Usuario registrado com sucesso" ) ));
    }
}

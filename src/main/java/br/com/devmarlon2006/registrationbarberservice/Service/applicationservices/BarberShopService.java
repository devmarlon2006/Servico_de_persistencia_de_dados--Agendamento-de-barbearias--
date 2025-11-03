package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.Repository.BarberShopRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.modelsassemblersmanagers.BarberShopAssembler;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.BarberShopRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.config.security.ApiAuthorization;
import br.com.devmarlon2006.registrationbarberservice.config.security.loginuserdetailsprovinders.BarberUserDetailsProvider;
import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos.BarberShopRegistrationDTO;
import lombok.NonNull;
import org.springframework.stereotype.Service;


@Service
public class BarberShopService {

    /*
     * Serviços solicitados
     */
    private final BarberShopRepositoryManager barberShopRepositoryManager;

    private final BarberShopAssembler barberShopAssembler;

    public BarberShopService(BarberShopRepositoryManager barberShopRepositoryManager, BarberShopAssembler barberShopAssembler)
    {
        this.barberShopRepositoryManager = barberShopRepositoryManager;
        this.barberShopAssembler = barberShopAssembler;
    }


    @NonNull
    public MessageContainer<MesagerComplements<String>> processBarberShopRegistration(BarberShopRegistrationDTO barberShopDTO) {

        MesagerComplements<BarberShop> barberShopOperation = barberShopAssembler.barberShopAssembler( barberShopDTO );

        if (barberShopOperation.getStatus() == ResponseStatus.ERROR) {

            return new MessageContainer<>(barberShopOperation.getStatus().getResponseMessage() , MesagerComplements.complementsOnlyBody(
                    OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ) ));

        }

        try{


            MesagerComplements<String> saveResponse = barberShopRepositoryManager.postOnRepository(barberShopOperation.getBody());

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

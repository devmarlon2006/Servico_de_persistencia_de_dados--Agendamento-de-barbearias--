package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberShopRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BaseManagerRepository.BaseRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.InputValidationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarberShopRepositoryManager extends BaseRepositoryManager<BarberShop> {

    private final BarberShopRepository barberShopRepository; // Repositorio do banco de dados (BarberShop)

    private final ConnectivityService connectivityService;


    public BarberShopRepositoryManager(BarberShopRepository barberShopRepository, ConnectivityService connectivityService) {
        this.barberShopRepository = barberShopRepository;
        this.connectivityService = connectivityService;
    }

    @Override
    public MesagerComplements<String> postOnRepository(BarberShop barberShopRecord) {

        try{

            if (repositoryGET( barberShopRecord, TypeOfReturn.NEGATIVE ).equals( ResponseStatus.WARNING ) ) {
                return MesagerComplements.complementsComplete( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNIQUE_CONSTRAINT.getFormattedMessage( "BarberShop" ));
            }

        }catch (NullPointerException e){
            return MesagerComplements.complementsComplete( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( e.getMessage() ));
        }

        if (barberShopRecord.getId() != null || barberShopRecord.getOwnerId() != null){

            try{

                barberShopRecord.generateId();
                barberShopRepository.save(barberShopRecord);

            } catch (Exception e) {

               return MesagerComplements.complementsComplete( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ));

            }

        }

        return MesagerComplements.complementsComplete( ResponseStatus.SUCCESS, OperationStatusCode.SUCCESS_ENTITY_CREATED.getFormattedMessage( "Usuario registrado com sucesso" ));

    }

    @Override
    public ResponseStatus repositoryGET(BarberShop barberShop, TypeOfReturn type){

       if (barberShopRepository.existsById( barberShop.getId() )
               || barberShopRepository.existsByAddress( barberShop.getAddress())
               || barberShopRepository.existsByPhone( barberShop.getPhone())) {

           return switch (type) {
             case NEGATIVE -> ResponseStatus.WARNING; //Para avisos, usado para casos de conflito;
             case POSITIVE -> ResponseStatus.SUCCESS; //Para Caso queira usar para algum fim de atribuição;
           };
        }
       return connectivityService.retrieveSuccess();
    }


    @Override
    public boolean validateAtributesInputs(BarberShop barberShop){
        List<Boolean> list = new ArrayList<>();

        list.add( InputValidationService.NameIsCorrect( barberShop.getName() ) );
        list.add( InputValidationService.MatchCharacter( barberShop.getId() ) );
        list.add( InputValidationService.PhoneIsCorrect( barberShop.getPhone() ) );

        return list.stream().allMatch( Boolean::booleanValue );
    }
}

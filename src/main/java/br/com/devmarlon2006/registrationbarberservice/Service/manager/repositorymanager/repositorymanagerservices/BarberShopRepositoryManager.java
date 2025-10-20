package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberShopRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BaseManagerRepository.BaseRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarberShopRepositoryManager extends BaseRepositoryManager<BarberShop> {

    //Repositorys
    private final BarberShopRepository barberShopRepository;

    private final ConnectivityService connectivityService;


    public BarberShopRepositoryManager(BarberShopRepository barberShopRepository, ConnectivityService connectivityService) {
        this.barberShopRepository = barberShopRepository;
        this.connectivityService = connectivityService;
    }

    @Override
    public MesagerComplements postOnRepository(BarberShop barberShopRecord) {

        try{

            if (repositoryGET( barberShopRecord, TypeOfReturn.NEGATIVE ).equals( ResponseStatus.WARNING ) ) {
                return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNIQUE_CONSTRAINT);
            }

        }catch (NullPointerException e){
            return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED);
        }

        if (barberShopRecord.getId() != null || barberShopRecord.getOwnerId() != null){

            try{

                barberShopRecord.generateId();
                barberShopRepository.save(barberShopRecord);

            } catch (Exception e) {
               return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED);
            }

        }

        return new MesagerComplements( ResponseStatus.SUCCESS, OperationStatusCode.SUCCESS_ENTITY_CREATED);

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
    public boolean validateAtribiutesFormat(BarberShop barberShop){
        List<Boolean> list = new ArrayList<>();

        list.add( Validation.NameIsCorrect( barberShop.getName() ) );
        list.add( Validation.MatchCharacter( barberShop.getId() ) );
        list.add( Validation.PhoneIsCorrect( barberShop.getPhone() ) );

        return list.stream().allMatch( Boolean::booleanValue );
    }
}

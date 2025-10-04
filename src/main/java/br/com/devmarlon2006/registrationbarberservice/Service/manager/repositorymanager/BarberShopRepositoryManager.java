package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberShopRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.SuperRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class BarberShopRepositoryManager implements SuperRepositoryManager<BarberShop, String> {

    //Repositorys
    private final BarberShopRepository barberShopRepository;

    private final TestConectivity testConectivity;


    public BarberShopRepositoryManager(BarberShopRepository barberShopRepository, TestConectivity testConectivity) {
        this.barberShopRepository = barberShopRepository;
        this.testConectivity = testConectivity;
    }

    @Override
    public ResponseMessages postOnRepository(BarberShop barberShopRecord){

        try{

            if (repositoryGET( barberShopRecord, TypeOfReturn.NEGATIVE ).equals( ResponseMessages.WARNING )){

                return ResponseMessages.ERROR;

            }

        }catch (NullPointerException e){
            return ResponseMessages.ERROR;
        }

        if (barberShopRecord.getId() != null || barberShopRecord.getOwnerId() != null){
            barberShopRepository.save(barberShopRecord);
        }

        return testConectivity.retrieveError();
    }

    @Override
    public ResponseMessages repositoryGET(BarberShop barberShop, TypeOfReturn type){

       if (barberShopRepository.existsById( barberShop.getId() )
               || barberShopRepository.existsByAddress( barberShop.getAddress())
               || barberShopRepository.existsByPhone( barberShop.getPhone())) {

           return switch (type) {
             case NEGATIVE -> ResponseMessages.WARNING; //Para avisos, usado para casos de conflito;
             case POSITIVE -> ResponseMessages.SUCCESS; //Para Caso queira usar para algum fim de atribuição;
           };
        }
       return testConectivity.retrieveSuccess();
    }


    @Override
    public boolean isInstance(Class<?> obj){
        return obj.isAssignableFrom(BarberShop.class);
    }

    @Override
    public boolean validateAtribiutesFormat(BarberShop barberShop){
        return true;
    }
}

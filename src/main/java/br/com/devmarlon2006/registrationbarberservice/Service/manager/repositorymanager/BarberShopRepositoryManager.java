package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberShopRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.SuperRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//implements SuperRepositoryManager<BarberShop, String>
@Service
public class BarberShopRepositoryManager  {

    //Repositorys
    private final BarberShopRepository barberShopRepository;

    private final TestConectivity testConectivity;


    public BarberShopRepositoryManager(BarberShopRepository barberShopRepository, TestConectivity testConectivity) {
        this.barberShopRepository = barberShopRepository;
        this.testConectivity = testConectivity;
    }

    //@Override
    public MesagerComplements<?> postOnRepository(BarberShop barberShopRecord){

        MesagerComplements<String> message = new MesagerComplements<>();

        try{

            if (repositoryGET( barberShopRecord, TypeOfReturn.NEGATIVE ).equals( ResponseMessages.WARNING )){

                message.setCause( ResponseMessages.ERROR );
                message.setMessage("Erro interno");
                return message;
            }

        }catch (NullPointerException e){
            message.setMessage("Erro inesperado");
            message.setMessage( "Erro interno ao tentar persistir o registro" );
            return message;
        }

        if (barberShopRecord.getId() != null || barberShopRecord.getOwnerId() != null){
            barberShopRepository.save(barberShopRecord);
            message.setMessage("Registro persistido com sucesso");
            message.setCause(ResponseMessages.SUCCESS);
        }

        return message;
    }

    //@Override
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


    //@Override
    public boolean isInstance(Class<?> obj){
        return obj.isAssignableFrom(BarberShop.class);
    }

    //@Override
    public boolean validateAtribiutesFormat(BarberShop barberShop){
        return true;
    }
}

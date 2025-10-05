package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberShopRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.SuperRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

//
@Service
public class BarberShopRepositoryManager implements SuperRepositoryManager<BarberShop, String> {

    //Repositorys
    private final BarberShopRepository barberShopRepository;

    private final TestConectivity testConectivity;


    public BarberShopRepositoryManager(BarberShopRepository barberShopRepository, TestConectivity testConectivity, DataSource dataSource) {
        this.barberShopRepository = barberShopRepository;
        this.testConectivity = testConectivity;
    }

    @Override
    public MesagerComplements<String> postOnRepository(BarberShop barberShopRecord){

        MesagerComplements<String> message = new MesagerComplements<>();

        try{

            if (repositoryGET( barberShopRecord, TypeOfReturn.NEGATIVE ) == ResponseMessages.WARNING) {

                message.setStatus( ResponseMessages.ERROR );
                message.setMessage("Erro interno - ID erro: bs10");
                return message;
            }

        }catch (NullPointerException e){
            message.setMessage( "Erro inesperado - ID Erro: bs11" );
            return message;
        }

        if (barberShopRecord.getId() != null || barberShopRecord.getOwnerId() != null){

            try{

                barberShopRepository.save(barberShopRecord);

                message.setMessage("Registro persistido com sucesso");
                message.setStatus(ResponseMessages.SUCCESS);

            } catch (NullPointerException e) {
                message.setMessage("Erro interno - ID Erro: bs12");
                message.setStatus(ResponseMessages.ERROR);
               return message;
            }

        }

        return message;
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
        List<Boolean> list = new ArrayList<>();

        list.add( Validation.NameIsCorrect( barberShop.getName() ) );
        list.add( Validation.MatchCharacter( barberShop.getId() ) );
        list.add( Validation.PhoneIsCorrect( barberShop.getPhone() ) );

        return list.stream().allMatch( Boolean::booleanValue );
    }
}

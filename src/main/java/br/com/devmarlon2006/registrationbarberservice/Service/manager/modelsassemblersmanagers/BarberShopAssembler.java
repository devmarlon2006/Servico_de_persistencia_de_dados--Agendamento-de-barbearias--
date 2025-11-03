package br.com.devmarlon2006.registrationbarberservice.Service.manager.modelsassemblersmanagers;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.Repository.BarberShopRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.config.security.loginuserdetailsprovinders.BarberUserDetailsProvider;
import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos.BarberShopRegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarberShopAssembler {


    private final BarberShopRepository barberShopRepository;
    private final BarberUserDetailsProvider barberUserDetailsProvider;
    private final BarberRepository barberRepository;

    public MesagerComplements<BarberShop> barberShopAssembler(BarberShopRegistrationDTO barberShopDTO ) {

        BarberShop barberShopRecord = BarberShop.buildFromRegistrationDTO( barberShopDTO );

        Barber barbe = barberRepository.findByEmail( barberUserDetailsProvider.getCurrentUserDetails().getEmail());

        if (barbe != null) {

            if (barberShopRepository.existsByOwnerId( barbe )) {
                return MesagerComplements.complementsComplete( ResponseStatus.ERROR , null );
            }

            barberShopRecord.setOwnerId( barbe );
        }

        return MesagerComplements.complementsComplete( ResponseStatus.SUCCESS , barberShopRecord );
    }

    public MesagerComplements<BarberShop> barberShopAssemblerNoBarberID(BarberShopRegistrationDTO barberShopDTO ) {
        BarberShop barberShopRecord = BarberShop.buildFromRegistrationDTO( barberShopDTO );
        barberShopRecord.generateId();
        return MesagerComplements.complementsComplete( ResponseStatus.SUCCESS , barberShopRecord );
    }


}

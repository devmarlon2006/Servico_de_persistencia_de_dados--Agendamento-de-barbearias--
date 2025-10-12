package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BarberPlusShopManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.model.DataTransferObject;
import org.springframework.stereotype.Service;

@Service
public class BarberAppointmentService {

    private final BarberPlusShopManager barberPlusShopManager;


    public BarberAppointmentService(BarberPlusShopManager barberPlusShopManager) {
        this.barberPlusShopManager = barberPlusShopManager;
    }


    public void Montagem (DataTransferObject data) {

        BarberShop barberShopRecord = (BarberShop) data.requiredType(BarberShop.class );
        Barber barberRecord = (Barber) data.requiredType(Barber.class);

        barberPlusShopManager.processBarberPlusShop(barberShopRecord, barberRecord);

    }
}

package br.com.devmarlon2006.registrationbarberservice.Service.manager.modelsassemblersmanagers;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barber.barberdto.BarberRegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarberAssembler {

    private final PasswordEncoder passwordEncoder;

    public MesagerComplements<Barber> barberAssembler(BarberRegistrationDTO barberDTO) {

        Barber barber = Barber.buildFromRegistrationDTO( barberDTO );
        barber.setPassword( passwordEncoder.encode( barber.getPassword() ) );
        barber.generateId(); barber.DeafullScore();

        return MesagerComplements.complementsComplete( ResponseStatus.SUCCESS , barber );
    }


}

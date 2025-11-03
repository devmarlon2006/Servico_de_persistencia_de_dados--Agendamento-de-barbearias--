package br.com.devmarlon2006.registrationbarberservice.config.security.loginuserdetailsprovinders;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarberUserDetailsProvider {

    private final BarberRepository barberRepository;

    public Barber getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return barberRepository.findByEmail(
                authentication.getName() // Email !!
        );
    }

}

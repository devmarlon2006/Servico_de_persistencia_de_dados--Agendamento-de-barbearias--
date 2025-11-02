package br.com.devmarlon2006.registrationbarberservice.config.security.customsuserdetails;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;


@Service
public class CustomBarberDetailsService implements UserDetailsService {


    private final BarberRepository barberRepo;

    public CustomBarberDetailsService(BarberRepository barberRepo) {
        this.barberRepo = barberRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("Email: " + email);

        Barber usuario = barberRepo.findByEmail( email );

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário com email: " + email  + " não encontrado!");
        }

        return User.builder()
                .username( usuario.getEmail() )
                .password( usuario.getPassword() )
                .roles( usuario.getRole().getRole() )
                .build();
    }
}

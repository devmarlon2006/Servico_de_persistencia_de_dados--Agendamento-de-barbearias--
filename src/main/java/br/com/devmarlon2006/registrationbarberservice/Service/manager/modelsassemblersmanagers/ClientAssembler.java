package br.com.devmarlon2006.registrationbarberservice.Service.manager.modelsassemblersmanagers;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.model.client.Client;
import br.com.devmarlon2006.registrationbarberservice.model.client.clientdtos.ClientRegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientAssembler {

    private final PasswordEncoder passwordEncoder;


    public MesagerComplements<Client> clientAssembler(ClientRegistrationDTO clientDTO) {

        Client client = Client.buildFromRegistrationDTO( clientDTO );

        client.setPassword( passwordEncoder.encode( client.getPassword() ) );

        if (client.getId() == null) {
            client.generateId();
        }

        return MesagerComplements.complementsComplete( ResponseStatus.SUCCESS, client );
    }


}

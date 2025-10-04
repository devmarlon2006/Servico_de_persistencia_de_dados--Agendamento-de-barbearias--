package br.com.devmarlon2006.registrationbarberservice.Service.apimessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class MesagerComplements<T> {
    private ResponseMessages cause;
    private T message;
}

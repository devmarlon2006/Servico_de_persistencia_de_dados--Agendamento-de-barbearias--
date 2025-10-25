package br.com.devmarlon2006.registrationbarberservice.Service.apimessage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor(staticName = "private")
public class MesagerComplements {

    @JsonIgnore
    private ResponseStatus status;

    private String message;

    public MesagerComplements( ResponseStatus Status , String message) {
        this.status = Status;
        this.message = message;
    }

    public MesagerComplements( String message) {
        this.message = message;
    }
}

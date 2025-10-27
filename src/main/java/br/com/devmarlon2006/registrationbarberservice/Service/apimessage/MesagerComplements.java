package br.com.devmarlon2006.registrationbarberservice.Service.apimessage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor(staticName = "private")
public class MesagerComplements <T> {

    @JsonIgnore
    private ResponseStatus status;

    private T body;

    public MesagerComplements( ResponseStatus Status , T body) {
        this.status = Status;
        this.body = body;
    }

    public MesagerComplements( T body) {
        this.body = body;
    }

    public MesagerComplements( ResponseStatus Status) {
        this.status = Status;
    }


    /*
     * Static factory Methods
     */

    public static <T> MesagerComplements<T> complementsOnlyBody (T body) {
        return new MesagerComplements<>(body);
    }

    public static <T> MesagerComplements<T> complementsOnlyStatus (ResponseStatus status) {
        return new MesagerComplements<>(status);
    }

    public static <T> MesagerComplements<T> complementsComplete (ResponseStatus status , T body) {
        return new MesagerComplements<>(status , body);
    }
}

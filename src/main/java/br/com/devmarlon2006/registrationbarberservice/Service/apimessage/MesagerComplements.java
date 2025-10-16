package br.com.devmarlon2006.registrationbarberservice.Service.apimessage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MesagerComplements {
    private ResponseMessages status;
    private StatusOperation message;
    private String messageResponse;

    public MesagerComplements(ResponseMessages status, StatusOperation message){
        this.status = status;
        this.message = message;
    }

    public MesagerComplements(ResponseMessages status, StatusOperation message, String messageResponse){
        this.status = status;
        this.message = message;
        this.messageResponse = messageResponse;
    }

    public boolean isSuccess(){
        return  this.status == ResponseMessages.SUCCESS;
    }

    public void Success(){
        this.status = ResponseMessages.SUCCESS;
        this.message = StatusOperation.SUCESS_EXPECTED;
        this.messageResponse = StatusOperation.SUCESS_EXPECTED.getFormattedMessage( StatusOperation.SUCESS_EXPECTED.getMessage() );
    }
}

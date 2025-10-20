package br.com.devmarlon2006.registrationbarberservice.Service.apimessage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MesagerComplements {
    private ResponseStatus status;
    private OperationStatusCode message;
    private String messageResponse;

    public MesagerComplements(ResponseStatus status, OperationStatusCode message){
        this.status = status;
        this.message = message;
    }

    public MesagerComplements(ResponseStatus status, OperationStatusCode message, String messageResponse){
        this.status = status;
        this.message = message;
        this.messageResponse = messageResponse;
    }

    public boolean isSuccess(){
        return  this.status == ResponseStatus.SUCCESS;
    }

    public void Success(){
        this.status = ResponseStatus.SUCCESS;
        this.message = OperationStatusCode.SUCESS_EXPECTED;
        this.messageResponse = OperationStatusCode.SUCESS_EXPECTED.getFormattedMessage( OperationStatusCode.SUCESS_EXPECTED.getMessage() );
    }
}

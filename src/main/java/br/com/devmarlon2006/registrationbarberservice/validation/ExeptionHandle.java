package br.com.devmarlon2006.registrationbarberservice.validation;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExeptionHandle {

    public ExeptionHandle() {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException exception) {

        MessageContainer<FieldError> validationErrorContainer = new MessageContainer<>();

        List<FieldError> fieldErrors = exception.getFieldErrors();

        validationErrorContainer.addList(fieldErrors);

        validationErrorContainer.addResponse("Erro de validação: Os dados enviados são inválidos");

        return ResponseEntity.badRequest().body(validationErrorContainer);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleException(AccessDeniedException exception) {
        MessageContainer<MesagerComplements<String>> validationErrorContainer = new MessageContainer<>();

        validationErrorContainer.addResponse("Acesso Negado");

        validationErrorContainer.addMessage(
            MesagerComplements.complementsOnlyBody(
                "Você não possui permissão para acessar este recurso. Verifique suas credenciais ou entre em contato com o administrador."
            )
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(validationErrorContainer);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeException(RuntimeException exception) {
        MessageContainer<MesagerComplements<String>> validationErrorContainer = new MessageContainer<>();

        validationErrorContainer.addResponse("Erro Interno do Servidor");

        validationErrorContainer.addMessage(
            MesagerComplements.complementsOnlyBody(
                "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde ou entre em contato com o suporte técnico."
            )
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(validationErrorContainer);
    }

}

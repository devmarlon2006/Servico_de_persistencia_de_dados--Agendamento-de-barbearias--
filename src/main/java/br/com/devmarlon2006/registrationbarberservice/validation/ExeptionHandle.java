package br.com.devmarlon2006.registrationbarberservice.validation;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import org.springframework.http.ResponseEntity;
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

        MessageContainer<FieldError> validationErroCOntainer = new MessageContainer<>();

        List<FieldError> fieldErrors = exception.getFieldErrors();

        validationErroCOntainer.addList( fieldErrors );

        validationErroCOntainer.addResponse(exception.getMessage());

        return ResponseEntity.badRequest().body(validationErroCOntainer);
    }
}

package com.api.politizei.exception;

import com.api.politizei.message.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({BusinessRuleException.class, BusinessSecurityException.class, NotFoundException.class})
    public ErrorMessage exceptionHandler(Exception e){
        return new ErrorMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessage methodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        String error = "O campo " + errors.stream().findFirst().get();
        error = error.concat(" é obrigatório");
        return new ErrorMessage(error, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
}

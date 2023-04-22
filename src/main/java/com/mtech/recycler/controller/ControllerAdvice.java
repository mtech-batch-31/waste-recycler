package com.mtech.recycler.controller;

import com.mtech.recycler.model.base.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;


@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<Object> handleConflict(ResponseStatusException ex, WebRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setReturnCode(String.valueOf(ex.getStatusCode().value()));
        baseResponse.setMessage(ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(baseResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        BaseResponse baseResponse = new BaseResponse();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            baseResponse.setMessage(errorMessage);
        });

        baseResponse.setReturnCode(String.valueOf(ex.getStatusCode().value()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }
}

package com.mtech.recycler.controller;

import com.mtech.recycler.dto.base.BaseResponseDto;
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
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setReturnCode(String.valueOf(ex.getStatusCode().value()));
        baseResponseDto.setMessage(ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(baseResponseDto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            baseResponseDto.setMessage(errorMessage);
        });

        baseResponseDto.setReturnCode(String.valueOf(ex.getStatusCode().value()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDto);
    }
}

package com.example.LeetcodeBox.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(exception = EntryExistsAlreadyException.class)
    public ResponseEntity<ResponseWrapperDto> EntryExistsAlreadyExceptionHandler(EntryExistsAlreadyException ex){
        ResponseWrapperDto responseWrapperDto=ResponseWrapperDto.builder()
        .status(HttpStatus.CONFLICT.value())
        .message(ex.getMessage())
        .build();
        return new ResponseEntity<>(responseWrapperDto,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(exception = EmptyEntityException.class)
    public ResponseEntity<ResponseWrapperDto> EmptyEntityExceptionHandler(EmptyEntityException ex){
        ResponseWrapperDto responseWrapperDto=ResponseWrapperDto.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .message(ex.getMessage())
        .build();
        return new ResponseEntity<>(responseWrapperDto,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

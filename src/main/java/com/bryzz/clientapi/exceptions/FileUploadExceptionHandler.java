package com.bryzz.clientapi.exceptions;

import com.bryzz.clientapi.domain.dto.MessageResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FileUploadExceptionHandler extends ResponseEntityExceptionHandler {
        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public ResponseEntity<MessageResponseDTO> handleMaxSizeException(MaxUploadSizeExceededException exc) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponseDTO("File too large!"));
        }

}

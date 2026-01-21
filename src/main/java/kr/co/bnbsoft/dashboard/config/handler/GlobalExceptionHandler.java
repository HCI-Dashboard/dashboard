package kr.co.bnbsoft.dashboard.config.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.retrotv.framework.foundation.common.exception.http.AccessDeniedException;
import dev.retrotv.framework.foundation.common.exception.http.AuthFailException;
import dev.retrotv.framework.foundation.common.exception.http.BadRequestException;
import dev.retrotv.framework.foundation.common.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handler(IllegalArgumentException ex) {
        ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(er.getHttpStatus()).body(er);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handler(AccessDeniedException ex) {
        ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
        return ResponseEntity.status(er.getHttpStatus()).body(er);
    }

    @ExceptionHandler(AuthFailException.class)
    public ResponseEntity<ErrorResponse> handler(AuthFailException ex) {
        ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(er.getHttpStatus()).body(er);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handler(BadRequestException ex) {
        ErrorResponse er = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(er.getHttpStatus()).body(er);
    }
}

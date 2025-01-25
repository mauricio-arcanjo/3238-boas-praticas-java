package br.com.alura.adopet.api.exception;

import org.springframework.http.ResponseEntity;

public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String message) {
        super(message);
    }
}

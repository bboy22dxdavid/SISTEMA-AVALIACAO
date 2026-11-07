package io.github.bboy22dxdavid.sang_backEnd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> tratarQualquerErro(Exception ex) {
        ex.printStackTrace(); // Imprime o erro completo no console do IntelliJ
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "erro", ex.getClass().getSimpleName(),
                        "mensagem", ex.getMessage() != null ? ex.getMessage() : "Erro sem descrição"
                ));
    }
}

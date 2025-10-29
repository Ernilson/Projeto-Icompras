package br.com.Icompras.pedidos.model.exceptions;

import lombok.Getter;

@Getter
public class ValidationsExceptions extends RuntimeException{

    private String fiel;

    private String message;

    public ValidationsExceptions(String fiel, String message) {
        super(message);
        this.fiel = fiel;
        this.message = message;
    }
}

package br.com.Icompras.pedidos.client.representation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

public record ProdutoRepresentation(Integer codigo,
                                    String nome,
                                    BigDecimal valorUnitario) {
}

package br.com.icompras.faturamento.entity;

import java.math.BigDecimal;

public record ItemPedido(Long codigo,
                         String descricao, BigDecimal valorUnitario, Integer quantidade) {
}

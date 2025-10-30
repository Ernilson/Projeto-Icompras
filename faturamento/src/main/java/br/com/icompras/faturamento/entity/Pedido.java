package br.com.icompras.faturamento.entity;

import java.math.BigDecimal;
import java.util.List;

public record Pedido(
        Long codigo, Cliente cliente, String data, BigDecimal total,
        List<ItemPedido> itens
) {
}

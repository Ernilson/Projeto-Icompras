package br.com.icompras.faturamento.subscriber.representation;

import java.math.BigDecimal;

public record DetalheItemPedidoRepresentation(
        Long codigoProduto,
        String nome,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal total
) {
    //public BigDecimal getTotal(){
       // return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    //}
}

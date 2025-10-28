package br.com.Icompras.pedidos.model;

import br.com.Icompras.pedidos.model.enums.TipoPagamento;
import lombok.Data;

@Data
public class DadosPagamentos {
   private String dados;
   private TipoPagamento tipoPagamento;
}

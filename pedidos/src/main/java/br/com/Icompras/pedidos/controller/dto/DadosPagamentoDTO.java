package br.com.Icompras.pedidos.controller.dto;

import br.com.Icompras.pedidos.model.enums.TipoPagamento;

public record DadosPagamentoDTO(String dados, TipoPagamento tipoPagamento) {
}

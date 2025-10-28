package br.com.Icompras.pedidos.service;

import br.com.Icompras.pedidos.client.ServicoBancarioClient;
import br.com.Icompras.pedidos.model.Pedido;
import br.com.Icompras.pedidos.repository.ItemPedidoRepository;
import br.com.Icompras.pedidos.repository.PedidoRepository;
import br.com.Icompras.pedidos.validator.Pedidovalidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final Pedidovalidator validator;
    private final ServicoBancarioClient servicoBancarioClient;


@Transactional
public Pedido criarPedido(Pedido pedido){
    validator.validar(pedido);
    realizarPersistencia(pedido);
    enviarSolicitacaoPagamento(pedido);
    return pedido;
  }

    private void enviarSolicitacaoPagamento(Pedido pedido) {
        var chavePagamento = servicoBancarioClient.solictarPagamento(pedido);
        pedido.setChavePagamento(chavePagamento);
    }

    private void realizarPersistencia(Pedido pedido) {
        repository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());
    }
}

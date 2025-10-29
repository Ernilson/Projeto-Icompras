package br.com.Icompras.pedidos.repository;

import br.com.Icompras.pedidos.model.ItemPedido;
import br.com.Icompras.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido>findByPedido(Pedido pedido);
}

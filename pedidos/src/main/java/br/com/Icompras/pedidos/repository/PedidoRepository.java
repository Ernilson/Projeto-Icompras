package br.com.Icompras.pedidos.repository;

import br.com.Icompras.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByCodigoAndChavePagamento(Long codigoPedido, String chavePagamento);
}

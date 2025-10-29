package br.com.Icompras.pedidos.service;

import br.com.Icompras.pedidos.client.ServicoBancarioClient;
import br.com.Icompras.pedidos.model.Pedido;
import br.com.Icompras.pedidos.model.enums.StatusPedido;
import br.com.Icompras.pedidos.repository.ItemPedidoRepository;
import br.com.Icompras.pedidos.repository.PedidoRepository;
import br.com.Icompras.pedidos.validator.Pedidovalidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
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

    public void atualizarStatusPagamento(Long codigoPedido, String chavePagamento, boolean sucesso, String observacoes) {

        Optional<Pedido> pedidoEncontrado = repository.findByCodigoAndChavePagamento(codigoPedido, chavePagamento);

        if (pedidoEncontrado.isEmpty()){
            var msg = String.format("Pedido não encontrado para o código %d e chave pgmto %s",
                    codigoPedido, chavePagamento);
            log.error(msg);
            return;
        }
        Pedido pedido = pedidoEncontrado.get();
        if (sucesso){
            pedido.setStatus(StatusPedido.PAGO);
        }else {
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
        }
        repository.save(pedido);
    }
}

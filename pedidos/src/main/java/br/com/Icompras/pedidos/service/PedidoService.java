package br.com.Icompras.pedidos.service;

import br.com.Icompras.pedidos.client.ServicoBancarioClient;
import br.com.Icompras.pedidos.controller.dto.DadosPagamentoDTO;
import br.com.Icompras.pedidos.model.DadosPagamentos;
import br.com.Icompras.pedidos.model.Pedido;
import br.com.Icompras.pedidos.model.enums.StatusPedido;
import br.com.Icompras.pedidos.model.enums.TipoPagamento;
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

    @Transactional
    public void adicionarNovoPagamento(Long codigoPedido, String dadosCartao,
                                       TipoPagamento tipo){
    var pedidoEncotrado = repository.findById(codigoPedido);
    if (pedidoEncotrado.isEmpty()){
        return;
    }

    var pedido = pedidoEncotrado.get();

    var dadosPagamentos = new DadosPagamentoDTO(dadosCartao, tipo);

        pedido.setDadosPagamentos(dadosPagamentos);
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setObservacoes("Novo pagamento realizado, aguardando o processamento");

        // Não seria necessario coloar esse repositori atualizando o pedido pois ele já se encontra dentro de uma transação
        repository.save(pedido);

        String novaChavePagamento = servicoBancarioClient.solictarPagamento(pedido);
        pedido.setChavePagamento(novaChavePagamento);
    }
}

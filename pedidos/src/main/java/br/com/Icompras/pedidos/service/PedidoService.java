package br.com.Icompras.pedidos.service;

import br.com.Icompras.pedidos.client.ClientesClient;
import br.com.Icompras.pedidos.client.ProdutosClient;
import br.com.Icompras.pedidos.client.ServicoBancarioClient;
import br.com.Icompras.pedidos.controller.dto.DadosPagamentoDTO;
import br.com.Icompras.pedidos.model.ItemPedido;
import br.com.Icompras.pedidos.model.Pedido;
import br.com.Icompras.pedidos.model.enums.StatusPedido;
import br.com.Icompras.pedidos.model.enums.TipoPagamento;
import br.com.Icompras.pedidos.model.exceptions.ItemNaoEncontradoException;
import br.com.Icompras.pedidos.publisher.PagamentoPublisher;
import br.com.Icompras.pedidos.repository.ItemPedidoRepository;
import br.com.Icompras.pedidos.repository.PedidoRepository;
import br.com.Icompras.pedidos.validator.Pedidovalidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final Pedidovalidator validator;
    private final ServicoBancarioClient servicoBancarioClient;
    private final ClientesClient apiClientes;
    private final ProdutosClient apiProdutos;
    private final PagamentoPublisher publisher;


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
            prepararEPublicarPedidoPago(pedido);
        }else {
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
        }
        repository.save(pedido);
    }

    private void prepararEPublicarPedidoPago(Pedido pedido) {
        pedido.setStatus(StatusPedido.PAGO);
        carregarDadosCliente(pedido);
        carregarDadosItensPedido(pedido);
        publisher.publicar(pedido);
    }

    @Transactional
    public void adicionarNovoPagamento(Long codigoPedido, String dadosCartao,
                                       TipoPagamento tipo){
    var pedidoEncotrado = repository.findById(codigoPedido);
    if (pedidoEncotrado.isEmpty()){
        throw new ItemNaoEncontradoException("Pedido não encontrado pare o código informado");
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

    public Optional<Pedido> carregarDadosCompletosPedido(Long codigo){
    Optional<Pedido> pedido = repository.findById(codigo);
    pedido.ifPresent(this::carregarDadosCliente);
    pedido.ifPresent(this::carregarDadosItensPedido);
    return pedido;
    }

    private void carregarDadosCliente(Pedido pedido){
        Long codigoCliente = pedido.getCodigoCliente();
        var response = apiClientes.obterDados(codigoCliente);
        pedido.setDadosCliente(response.getBody());
    }

    private void carregarDadosItensPedido(Pedido pedido){
       List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);
       pedido.setItens(itens);
       pedido.getItens().forEach(this::carregarDadosProduto);
    }

    private void carregarDadosProduto(ItemPedido itemPedido){
        Long codigoProduto = itemPedido.getCodigoProduto();
        var response = apiProdutos.obterDados(codigoProduto);
        itemPedido.setNome(response.getBody().nome());
    }
}

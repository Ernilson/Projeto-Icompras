package br.com.Icompras.pedidos.validator;

import br.com.Icompras.pedidos.client.ClientesClient;
import br.com.Icompras.pedidos.client.ProdutosClient;
import br.com.Icompras.pedidos.client.representation.ClienteRepresentation;
import br.com.Icompras.pedidos.client.representation.ProdutoRepresentation;
import br.com.Icompras.pedidos.model.ItemPedido;
import br.com.Icompras.pedidos.model.Pedido;
import feign.Feign;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Pedidovalidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

    public void validar(Pedido pedido){
        Long codigoCliente = pedido.getCodigoCliente();
        validarCliente(codigoCliente);
        pedido.getItens().forEach(this::validarItem);

    }

    private void validarCliente(Long codigoCliente){
        try {
            var reponse = clientesClient.obterDados(codigoCliente);
            ClienteRepresentation cliente = reponse.getBody();
            log.info("Cliente de código {} encontrado: {}", cliente.codigo(), cliente.nome());
        } catch (FeignException.NotFound e) {
            log.error("Cliente não encontrado!");
        }

    }

    private void validarItem(ItemPedido item){
        try {
            var response = produtosClient.obterDados(item.getCodigoProduto());
            ProdutoRepresentation produto = response.getBody();
            log.info("Produto de código {} encontrado: {}", produto.codigo(), produto.nome());
        } catch (FeignException.NotFound e) {
            log.error("Produto não encontrado!");
        }
    }

    //        List<Long> codigoProdutos =
//                pedido.getItens()
//                        .stream()
//                        .map(i -> i.getCodigoProduto()).toList();
//
//        codigoProdutos.forEach(codigoProduto ->{
//            ResponseEntity<ProdutoRepresentation>response = produtosClient.obterDados(codigoProduto);
//            ProdutoRepresentation produto = response.getBody();
//        });
}

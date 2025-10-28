package br.com.Icompras.pedidos.validator;

import br.com.Icompras.pedidos.client.ClientesClient;
import br.com.Icompras.pedidos.client.ProdutosClient;
import br.com.Icompras.pedidos.client.representation.ProdutoRepresentation;
import br.com.Icompras.pedidos.model.ItemPedido;
import br.com.Icompras.pedidos.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Pedidovalidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

    public void validar(Pedido pedido){
        Long codigoCliente = pedido.getCodigoCliente();
        validarCliente(codigoCliente);
        pedido.getItens().forEach(this::validarItem);

    }

    private void validarCliente(Long codigoCliente){

    }

    private void validarItem(ItemPedido item){

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

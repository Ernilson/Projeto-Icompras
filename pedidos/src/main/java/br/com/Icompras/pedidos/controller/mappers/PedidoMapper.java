package br.com.Icompras.pedidos.controller.mappers;

import br.com.Icompras.pedidos.controller.dto.ItemPedidoDTO;
import br.com.Icompras.pedidos.controller.dto.NovoPedidoDTO;
import br.com.Icompras.pedidos.model.ItemPedido;
import br.com.Icompras.pedidos.model.Pedido;
import br.com.Icompras.pedidos.model.enums.StatusPedido;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    ItemPedidoMapper ITEM_PEDIDO_MAPPER = Mappers.getMapper(ItemPedidoMapper.class);

    @Mapping(source = "itens", target = "itens", qualifiedByName = "mapItens")

    // Mapeia o campo "chavePix" de dentro do "dadosPagamentoDTO" para o campo "chavePagamento" do Pedido
    @Mapping(source = "dadosPagamentoDTO.dados", target = "chavePagamento")

    // Mapeia os outros campos do DTO para a entidade
    @Mapping(source = "codigoCliente", target = "codigoCliente")

    // Diz ao MapStruct para ignorar o 'codigo' e 'status', pois serão definidos no Service
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataPedido", ignore = true) // O banco de dados define (default now())
    @Mapping(target = "total", ignore = true) // Será calculado no Service
    @Mapping(target = "codigoRastreio", ignore = true) // Nulo na criação
    @Mapping(target = "urlNf", ignore = true) // Nulo na criação
    Pedido map(NovoPedidoDTO dto);

    @Named("mapItens")
    default List<ItemPedido> mapItens(List<ItemPedidoDTO> dtos){
        // AVISO: Certifique-se que seu ItemPedidoMapper ignora "codigo" e "pedido"
        // como fizemos na interação anterior.
        return dtos.stream().map(ITEM_PEDIDO_MAPPER::map).toList();
    }

    @AfterMapping
    default void afterMapping(@MappingTarget Pedido pedido){
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setDataPedido(LocalDateTime.now());

        var total = calcularTotal(pedido);

        pedido.setTotal(total);

        pedido.getItens().forEach(item -> item.setPedido(pedido));
    }

    private static BigDecimal calcularTotal(Pedido pedido) {

        // 1. Se a lista de itens for nula ou vazia, o total é ZERO.
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 2. Se a lista existir, calcula o total
        return pedido.getItens().stream()
                .map(item -> {
                    // 3. Verifica se os valores dentro do item são nulos
                    BigDecimal valor = (item.getValorUnitario() != null) ? item.getValorUnitario() : BigDecimal.ZERO;
                    Integer qtd = (item.getQuantidade() != null) ? item.getQuantidade() : 0;

                    // 4. Multiplica (valor * quantidade) para cada item
                    return valor.multiply(new BigDecimal(qtd));
                })
                // 5. Soma todos os subtotais e retorna o total
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
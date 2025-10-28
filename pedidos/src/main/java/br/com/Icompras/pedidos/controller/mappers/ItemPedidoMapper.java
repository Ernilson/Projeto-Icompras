package br.com.Icompras.pedidos.controller.mappers;

import br.com.Icompras.pedidos.controller.dto.ItemPedidoDTO;
import br.com.Icompras.pedidos.model.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // <-- 1. ADICIONE ESTE IMPORT

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    // --- CORREÇÃO AQUI ---
    // 2. Ignora o 'codigo', pois será gerado pelo banco de dados
    @Mapping(target = "codigo", ignore = true)
    // 3. Ignora o 'pedido', pois será associado manualmente no Service
    @Mapping(target = "pedido", ignore = true)
    // --- FIM DA CORREÇÃO ---
    ItemPedido map(ItemPedidoDTO dto);
}
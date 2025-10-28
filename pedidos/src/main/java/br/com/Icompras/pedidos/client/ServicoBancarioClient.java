package br.com.Icompras.pedidos.client;

import br.com.Icompras.pedidos.model.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class ServicoBancarioClient {

    public String solictarPagamento(Pedido pedido){
        log.info("Solicitando pagamento para o pedido de código: {}", pedido.getCodigo());
        return UUID.randomUUID().toString();
    }
}

package br.com.Icompras.pedidos.publisher;

import br.com.Icompras.pedidos.model.Pedido;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoPublisher {

    private final DetalhePedidoMapper detalhePedidoMapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${icompras.config.kafka.topics.pedidos-pagos}")
    private String topico;

    public void publicar(Pedido pedido){
        log.info("Publicando pedido pago{}", pedido.getCodigo());

        try {
            var representation = detalhePedidoMapper.map(pedido);
            var json = objectMapper.writeValueAsString(representation);
            kafkaTemplate.send(topico, "dados", json);
        } catch (JsonParseException e) {
            log.error("Error ao processar o joson", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            log.error("Error técnico ao publicar no tópico de pedidos", e);
        }
    }
}

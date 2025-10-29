package br.com.Icompras.pedidos.controller;


import br.com.Icompras.pedidos.controller.dto.NovoPedidoDTO;
import br.com.Icompras.pedidos.controller.mappers.PedidoMapper;
import br.com.Icompras.pedidos.model.exceptions.ErroResposta;
import br.com.Icompras.pedidos.model.exceptions.ValidationsExceptions;
import br.com.Icompras.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;
    private final PedidoMapper mapper;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody NovoPedidoDTO dto){
        try {
        var pedido = mapper.map(dto);
        var novoPedido = service.criarPedido(pedido);

        // Constrói a URI do novo recurso (ex: http://localhost:8080/pedidos/1)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoPedido.getCodigo())
                .toUri();

        // Retorna 201 Created com a URI no cabeçalho e o objeto salvo no corpo
        return ResponseEntity.created(location).body(novoPedido);
        } catch (ValidationsExceptions e) {
            var erro = new ErroResposta("Erro validação", e.getFiel(), e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

}

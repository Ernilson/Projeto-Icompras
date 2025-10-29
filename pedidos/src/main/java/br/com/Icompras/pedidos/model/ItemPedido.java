package br.com.Icompras.pedidos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    /**
     * Mapeia a chave estrangeira 'codigo_pedido'.
     * @ManyToOne: Indica que muitos ItensPedido podem estar associados a um Pedido.
     * @JoinColumn: Especifica que a coluna 'codigo_pedido' nesta tabela é usada
     * para a junção (chave estrangeira).
     * FetchType.LAZY: Boa prática para performance. O Pedido associado só será
     * carregado do banco de dados quando você explicitamente
     * chamar o método itemPedido.getPedido().
     */

    @ManyToOne
    @JoinColumn(name = "codigo_pedido")
    @JsonBackReference
    private Pedido pedido;

    @Column(name = "codigo_produto", nullable = false)
    private Long codigoProduto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorUnitario;

    @Transient //nome do produto
    private String nome;
}

package br.com.Icompras.pedidos.model;

import br.com.Icompras.pedidos.client.representation.ClienteRepresentation;
import br.com.Icompras.pedidos.controller.dto.DadosPagamentoDTO;
import br.com.Icompras.pedidos.model.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @Column(name = "codigo_cliente")
    private Long codigoCliente;

    @CreationTimestamp
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;

    @Column(name = "chave_pagamento", columnDefinition = "TEXT")
    private String chavePagamento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column( precision = 16, scale = 2)
    private BigDecimal total;

    @Column(name = "codigo_rastreio", length = 255)
    private String codigoRastreio;

    @Column(name = "url_nf", columnDefinition = "TEXT")
    private String urlNf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @Transient
    private DadosPagamentoDTO dadosPagamentos;

    //@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "pedido")
    @JsonManagedReference
    private List<ItemPedido> itens = new ArrayList<>();

    @Transient
    private ClienteRepresentation dadosCliente;

}

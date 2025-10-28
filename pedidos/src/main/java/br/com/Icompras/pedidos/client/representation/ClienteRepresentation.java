package br.com.Icompras.pedidos.client.representation;

public record ClienteRepresentation(Integer codigo,
                                    String nome,
                                    String cpf,
                                    String logradouro,
                                    String numero,
                                    String bairro,
                                    String email,
                                    String telefone) {
}

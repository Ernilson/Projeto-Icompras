package br.com.icompras.clientes.service;


import br.com.icompras.clientes.model.Cliente;
import br.com.icompras.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public Cliente salvar(Cliente cliente){
        return repository.save(cliente);
    }

    public Optional<Cliente> obterCliente(Long codigo){
        return repository.findById(codigo);
    }
}

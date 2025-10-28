package br.com.icompras.produtos.service;


import br.com.icompras.produtos.model.Produto;
import br.com.icompras.produtos.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public Produto salvar(Produto produto){
        return repository.save(produto);
    }

    public Optional<Produto> obterPorCodigo(Long codigo){
        return repository.findById(codigo);
    }
}

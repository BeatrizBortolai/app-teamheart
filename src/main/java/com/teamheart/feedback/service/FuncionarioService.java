package com.teamheart.feedback.service;

import com.teamheart.feedback.entity.Funcionario;
import com.teamheart.feedback.exception.FuncionarioNaoEncontradoException;
import com.teamheart.feedback.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public Funcionario salvar(Funcionario funcionario) {
        return repository.save(funcionario);
    }

    public List<Funcionario> listar() {
        return repository.findAll();
    }

    public Optional<Funcionario> buscarPorId(UUID id) {
        return repository.findById(id);
    }

    public Funcionario atualizar(UUID id, Funcionario dadosAtualizados) {
        Funcionario funcionario = repository.findById(id)
            .orElseThrow(() -> new FuncionarioNaoEncontradoException("Funcionário não encontrado."));

        funcionario.setNome(dadosAtualizados.getNome());
        funcionario.setCargo(dadosAtualizados.getCargo());
        funcionario.setGenero(dadosAtualizados.getGenero());
        funcionario.setPcd(dadosAtualizados.getPcd());
        funcionario.setDepartamento(dadosAtualizados.getDepartamento());
        funcionario.setEtnia(dadosAtualizados.getEtnia());
        funcionario.setIdade(dadosAtualizados.getIdade());

        return repository.save(funcionario);
    }

    public void excluir(UUID id) {
        if (!repository.existsById(id)) {
            throw new FuncionarioNaoEncontradoException("Funcionário não encontrado.");
        }
        repository.deleteById(id);
    }
}

package com.teamheart.recrutamento.service;

import com.teamheart.recrutamento.dto.VagaRequest;
import com.teamheart.recrutamento.entity.Vaga;
import com.teamheart.recrutamento.exception.VagaNotFoundException;
import com.teamheart.recrutamento.repository.VagaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VagaService {

    private final VagaRepository repository;

    public VagaService(VagaRepository repository) {
        this.repository = repository;
    }

    public Vaga salvar(VagaRequest dto) {
        Vaga v = new Vaga();
        v.setTitulo(dto.titulo());
        v.setDescricao(dto.descricao());
        v.setDepartamento(dto.departamento());
        v.setNivel(dto.nivel());
        v.setMetaDiversidade(dto.metaDiversidade());
        return repository.save(v);
    }

    public List<Vaga> listarTodas() {
        return repository.findAll();
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new VagaNotFoundException("Vaga não encontrada com id: " + id);
        }
        repository.deleteById(id);
    }

}

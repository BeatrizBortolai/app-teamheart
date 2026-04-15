package com.teamheart.recrutamento.service;

import com.teamheart.login.exception.EmailJaCadastradoException;
import com.teamheart.recrutamento.dto.CandidatoRequest;
import com.teamheart.recrutamento.entity.Candidato;
import com.teamheart.recrutamento.exception.CandidatoNotFoundException;
import com.teamheart.recrutamento.repository.CandidatoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoService {

    private final CandidatoRepository repository;

    public CandidatoService(CandidatoRepository repository) {
        this.repository = repository;
    }

    public Candidato salvar(CandidatoRequest dto) {
        if (repository.existsByEmailIgnoreCase(dto.email())) {
            throw new EmailJaCadastradoException("Já existe um candidato cadastrado com o e-mail informado.");
        }

        Candidato c = new Candidato();
        c.setNome(dto.nome());
        c.setEmail(dto.email());
        c.setGenero(dto.genero());
        c.setEtnia(dto.etnia());
        c.setLocalizacao(dto.localizacao());
        c.setExperienciaAnos(dto.experienciaAnos());
        return repository.save(c);
    }

    public List<Candidato> listarTodos() {
        return repository.findAll();
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new CandidatoNotFoundException("Candidato não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}

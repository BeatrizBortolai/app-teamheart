package com.teamheart.recrutamento.service;

import com.teamheart.recrutamento.entity.Candidato;
import com.teamheart.recrutamento.entity.Selecao;
import com.teamheart.recrutamento.entity.Vaga;
import com.teamheart.recrutamento.exception.RecursoNaoEncontradoException;
import com.teamheart.recrutamento.repository.CandidatoRepository;
import com.teamheart.recrutamento.repository.SelecaoRepository;
import com.teamheart.recrutamento.repository.VagaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelecaoService {

    private final SelecaoRepository selecaoRepository;
    private final VagaRepository vagaRepository;
    private final CandidatoRepository candidatoRepository;

    public SelecaoService(SelecaoRepository selecaoRepository, VagaRepository vagaRepository,
                          CandidatoRepository candidatoRepository) {
        this.selecaoRepository = selecaoRepository;
        this.vagaRepository = vagaRepository;
        this.candidatoRepository = candidatoRepository;
    }

    public Selecao registrarSelecao(Long idCandidato, Long idVaga) {
        Candidato candidato = candidatoRepository.findById(idCandidato)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Candidato não encontrado: " + idCandidato));

        Vaga vaga = vagaRepository.findById(idVaga)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Vaga não encontrada: " + idVaga));

        Selecao selecao = new Selecao();
        selecao.setCandidato(candidato);
        selecao.setVaga(vaga);
        selecao.setStatus("PENDENTE");
        selecao.setPriorizado(avaliarDiversidade(candidato));

        return selecaoRepository.save(selecao);
    }

    private char avaliarDiversidade(Candidato candidato) {
        if (candidato.getGenero() != null && !candidato.getGenero().equalsIgnoreCase("masculino")) {
            return 'S';
        }
        return 'N';
    }

    public List<Selecao> listarTodas() {
        return selecaoRepository.findAll();
    }
}

package com.teamheart.recrutamento.service;

import com.teamheart.recrutamento.entity.Candidato;
import com.teamheart.recrutamento.entity.Selecao;
import com.teamheart.recrutamento.entity.Vaga;
import com.teamheart.recrutamento.exception.RecursoNaoEncontradoException;
import com.teamheart.recrutamento.repository.CandidatoRepository;
import com.teamheart.recrutamento.repository.SelecaoRepository;
import com.teamheart.recrutamento.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelecaoServiceTest {

    @Mock
    private SelecaoRepository selecaoRepository;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private CandidatoRepository candidatoRepository;

    private SelecaoService selecaoService;

    @BeforeEach
    void setUp() {
        selecaoService = new SelecaoService(selecaoRepository, vagaRepository, candidatoRepository);
    }

    @Test
    void devePriorizarCandidatoNaoMasculino() {
        Candidato candidato = new Candidato();
        candidato.setGenero("feminino");
        Vaga vaga = new Vaga();

        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(vagaRepository.findById(2L)).thenReturn(Optional.of(vaga));
        when(selecaoRepository.save(any(Selecao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Selecao selecao = selecaoService.registrarSelecao(1L, 2L);

        assertEquals('S', selecao.getPriorizado());
        assertEquals("PENDENTE", selecao.getStatus());
    }

    @Test
    void deveMarcarComoNaoPriorizadoQuandoGeneroForMasculino() {
        Candidato candidato = new Candidato();
        candidato.setGenero("masculino");
        Vaga vaga = new Vaga();

        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(vagaRepository.findById(2L)).thenReturn(Optional.of(vaga));
        when(selecaoRepository.save(any(Selecao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Selecao selecao = selecaoService.registrarSelecao(1L, 2L);

        assertEquals('N', selecao.getPriorizado());
    }

    @Test
    void deveLancarExcecaoQuandoCandidatoNaoExistir() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
            () -> selecaoService.registrarSelecao(1L, 2L));
    }
}

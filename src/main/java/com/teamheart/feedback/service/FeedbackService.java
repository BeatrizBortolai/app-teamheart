package com.teamheart.feedback.service;

import com.teamheart.feedback.dto.FeedbackRequestDTO;
import com.teamheart.feedback.dto.FeedbackResponseDTO;
import com.teamheart.feedback.entity.Feedback;
import com.teamheart.feedback.entity.Funcionario;
import com.teamheart.feedback.exception.FeedbackNaoEncontradoException;
import com.teamheart.feedback.exception.FuncionarioNaoEncontradoException;
import com.teamheart.feedback.repository.FeedbackRepository;
import com.teamheart.feedback.repository.FuncionarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackService.class);
    private final FeedbackRepository repository;
    private final FuncionarioRepository funcionarioRepository;

    public FeedbackService(FeedbackRepository repository, FuncionarioRepository funcionarioRepository) {
        this.repository = repository;
        this.funcionarioRepository = funcionarioRepository;
    }

    private FeedbackResponseDTO mapToResponseDTO(Feedback feedback) {
        Funcionario funcionario = feedback.getFuncionario();

        Integer idade = funcionario != null ? funcionario.getIdade() : null;
        String cargo = funcionario != null ? funcionario.getCargo() : "N/A";
        String depto = funcionario != null ? funcionario.getDepartamento() : "N/A";

        return new FeedbackResponseDTO(
            feedback.getId(),
            feedback.getSentimento(),
            feedback.getComentario(),
            feedback.getData(),
            idade,
            cargo,
            depto
        );
    }

    public FeedbackResponseDTO salvar(FeedbackRequestDTO requestDTO) {
        Funcionario funcionario = funcionarioRepository.findById(requestDTO.idFuncionario())
            .orElseThrow(() -> new FuncionarioNaoEncontradoException("Funcionário não encontrado com ID: "
                + requestDTO.idFuncionario()));

        Feedback feedback = new Feedback(
            requestDTO.sentimento(),
            requestDTO.comentario(),
            funcionario
        );
        feedback.setData(LocalDate.now());

        log.info("Salvando novo feedback. ID de Funcionário (Interno): {}", funcionario.getId());
        Feedback salvo = repository.save(feedback);

        return mapToResponseDTO(salvo);
    }

    public List<FeedbackResponseDTO> listarTodos() {
        log.info("Listando todos os feedbacks com sigilo garantido");
        return repository.findAllWithFuncionario().stream()
            .map(this::mapToResponseDTO)
            .toList();
    }

    public FeedbackResponseDTO buscarPorId(UUID id) {
        log.info("Buscando feedback com ID: {}", id);
        Feedback feedback = repository.findById(id)
            .orElseThrow(() -> new FeedbackNaoEncontradoException("Feedback não encontrado com ID: " + id));

        return mapToResponseDTO(feedback);
    }

    public FeedbackResponseDTO atualizar(UUID id, FeedbackRequestDTO dados) {
        log.info("Atualizando feedback com ID: {}", id);

        Feedback existente = repository.findById(id)
            .orElseThrow(() -> new FeedbackNaoEncontradoException("Feedback não encontrado com ID: " + id));

        existente.setSentimento(dados.sentimento());
        existente.setComentario(dados.comentario());

        if (dados.idFuncionario() != null && !dados.idFuncionario().equals(existente.getFuncionario().getId())) {
            Funcionario novoFuncionario = funcionarioRepository.findById(dados.idFuncionario())
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(
                    "Novo funcionário para atualização não encontrado."));
            existente.setFuncionario(novoFuncionario);
        }

        existente.setData(LocalDate.now());
        Feedback atualizado = repository.save(existente);

        log.info("Feedback ID {} atualizado com sucesso", id);
        return mapToResponseDTO(atualizado);
    }

    public void deletar(UUID id) {
        log.info("Excluindo feedback com ID: {}", id);

        Feedback existente = repository.findById(id)
            .orElseThrow(() -> new FeedbackNaoEncontradoException("Feedback não encontrado com ID: " + id));

        repository.delete(existente);
        log.info("Feedback ID {} excluído com sucesso", id);
    }
}

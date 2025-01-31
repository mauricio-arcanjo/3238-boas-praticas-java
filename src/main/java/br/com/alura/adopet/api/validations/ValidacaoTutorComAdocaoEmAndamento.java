package br.com.alura.adopet.api.validations;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacaoTutorComAdocaoEmAndamento implements  ValidacaoSolicitacaoAdocao {

    private final AdocaoRepository adocaoRepository;

    public ValidacaoTutorComAdocaoEmAndamento(AdocaoRepository adocaoRepository, TutorRepository tutorRepository) {
        this.adocaoRepository = adocaoRepository;
    }

    @Override
    public void validar(SolicitacaoAdocaoDto dto) {

        boolean tutorTemAdocaoEmAndamento = adocaoRepository
                .existsByTutorIdAndStatus(dto.idTutor(), StatusAdocao.AGUARDANDO_AVALIACAO);

        if (tutorTemAdocaoEmAndamento) {
            throw new ValidacaoException("Tutor já possui outra adoção aguardando avaliação!");
        }
    }
}

package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.*;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validations.ValidacaoSolicitacaoAdocao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdocaoService {

    private final AdocaoRepository adocaoRepository;
    private final EmailService emailService;
    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;
    private final List<ValidacaoSolicitacaoAdocao> validacoes;

    public AdocaoService(AdocaoRepository adocaoRepository, EmailService emailService, PetRepository petRepository, AbrigoRepository abrigoRepository, TutorRepository tutorRepository, List<ValidacaoSolicitacaoAdocao> validacoes) {
        this.adocaoRepository = adocaoRepository;
        this.emailService = emailService;
        this.petRepository = petRepository;
        this.tutorRepository = tutorRepository;
        this.validacoes = validacoes;
    }

    @Transactional
    public void solicitar(SolicitacaoAdocaoDto dto){

        Pet pet = petRepository.getReferenceById(dto.idPet());
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());

        validacoes.forEach(v -> v.validar(dto));

        Adocao adocao = new Adocao();
        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocao.setPet(pet);
        adocao.setTutor(tutor);
        adocao.setMotivo(dto.motivo());
        adocaoRepository.save(adocao);

//        emailService.enviarEmail(
//                adocao.getPet().getAbrigo().getEmail(),
//                "Solicitação de adoção",
//                "Olá " + adocao.getPet().getAbrigo().getNome() +"!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocao.getPet().getNome() +". \nFavor avaliar para aprovação ou reprovação."
//        );

    }

    @Transactional
    public void aprovar(AprovacaoAdocaoDto dto){
        Adocao adocao = adocaoRepository.getReferenceById(dto.idAdocao());
        adocao.setStatus(StatusAdocao.APROVADO);

//        emailService.enviarEmail(
//                adocao.getTutor().getEmail(),
//                "Adoção aprovada",
//                "Parabéns " + adocao.getTutor().getNome() +"!\n\nSua adoção do pet " + adocao.getPet().getNome() +", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +", foi aprovada.\nFavor entrar em contato com o abrigo " + adocao.getPet().getAbrigo().getNome() +" para agendar a busca do seu pet."
//        );
    }

    @Transactional
    public void reprovar(ReprovacaoAdocaoDto dto){
        Adocao adocao = adocaoRepository.getReferenceById(dto.idAdocao());
        adocao.setStatus(StatusAdocao.REPROVADO);
        adocao.setJustificativaStatus(dto.justificativaStatus());

//        emailService.enviarEmail(
//                adocao.getTutor().getEmail(),
//                "Adoção reprovada",
//                "Olá " + adocao.getTutor().getNome() +"!\n\nInfelizmente sua adoção do pet " + adocao.getPet().getNome() +", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +", foi reprovada pelo abrigo " + adocao.getPet().getAbrigo().getNome() +" com a seguinte justificativa: " + adocao.getJustificativaStatus()
//        );
    }

}

package br.com.alura.adopet.api.dto;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Tutor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record DetalhesTutorDto(
        Long id,

        String nome,

        String telefone,

        String email,

        List<Adocao> adocoes
) {
    public DetalhesTutorDto(Tutor tutor) {
        this(tutor.getId(), tutor.getNome(), tutor.getTelefone(), tutor.getEmail(), tutor.getAdocoes());
    }
}

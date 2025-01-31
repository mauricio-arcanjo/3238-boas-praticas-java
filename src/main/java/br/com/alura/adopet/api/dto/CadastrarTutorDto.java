package br.com.alura.adopet.api.dto;

import br.com.alura.adopet.api.model.Tutor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CadastrarTutorDto(
        @NotBlank
        String nome,

        @NotBlank
        @Pattern(regexp = "\\(?\\d{2}\\)?\\d?\\d{4}-?\\d{4}")
        String telefone,

        @NotBlank
        String email
) {
    public CadastrarTutorDto(Tutor tutor) {
        this(tutor.getNome(), tutor.getTelefone(), tutor.getEmail());
    }
}

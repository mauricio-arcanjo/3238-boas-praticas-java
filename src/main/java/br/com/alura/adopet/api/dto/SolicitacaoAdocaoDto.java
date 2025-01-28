package br.com.alura.adopet.api.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoAdocaoDto(

        @NotNull
//        @JsonManagedReference("adocao_pets")
        Long idPet,

        @NotNull
//        @JsonBackReference("tutor_adocoes")
        Long idTutor,

        @NotBlank
        String motivo
) {
}

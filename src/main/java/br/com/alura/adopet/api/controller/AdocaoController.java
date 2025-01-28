package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.DetalhesPetDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.service.AdocaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    private final AdocaoService adocaoService;

    public AdocaoController(AdocaoService adocaoService) {
        this.adocaoService = adocaoService;
    }

    @PostMapping
    public ResponseEntity<String> solicitar(@RequestBody @Valid SolicitacaoAdocaoDto dto) {
        try {
            adocaoService.solicitar(dto);
            return ResponseEntity.ok("Adoção solicitada com sucesso!");
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/aprovar")
    public ResponseEntity<String> aprovar(@RequestBody @Valid AprovacaoAdocaoDto dto) {
        adocaoService.aprovar(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reprovar")
    public ResponseEntity<String> reprovar(@RequestBody @Valid ReprovacaoAdocaoDto dto) {
        adocaoService.reprovar(dto);
        return ResponseEntity.ok().build();
    }

}

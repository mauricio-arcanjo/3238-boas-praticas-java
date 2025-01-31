package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastrarTutorDto;
import br.com.alura.adopet.api.dto.DetalhesTutorDto;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid CadastrarTutorDto dto) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(dto.telefone());
        boolean emailJaCadastrado = repository.existsByEmail(dto.email());

        if (telefoneJaCadastrado || emailJaCadastrado) {
            return ResponseEntity.badRequest().body("Dados já cadastrados para outro tutor!");
        } else {
            repository.save(new Tutor(dto.nome(), dto.telefone(), dto.email()));
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid CadastrarTutorDto dto) {
        repository.save(new Tutor(dto.nome(), dto.telefone(), dto.email()));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<DetalhesTutorDto>> listar(){
        List<DetalhesTutorDto> tutores = repository.findAll()
                .stream().map(DetalhesTutorDto::new).toList();
        return ResponseEntity.ok(tutores);

    }

}

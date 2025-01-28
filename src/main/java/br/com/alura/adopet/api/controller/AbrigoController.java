package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.service.AbrigoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abrigos")
public class AbrigoController {

    private final AbrigoService abrigoService;

    public AbrigoController(AbrigoService abrigoService) {
        this.abrigoService = abrigoService;
    }

    @GetMapping
    public ResponseEntity<List<Abrigo>> listar() {
        return  ResponseEntity.ok(abrigoService.listar());
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Abrigo abrigo) {
        try {
            abrigoService.cadastrar(abrigo);
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idOuNome}/pets")
    public ResponseEntity<List<Pet>> listarPets(@PathVariable String idOuNome) {
        List<Pet> pets = abrigoService.listarPets(idOuNome);
        return pets != null ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{idOuNome}/pets")
    @Transactional
    public ResponseEntity<String> cadastrarPet(@PathVariable String idOuNome, @RequestBody @Valid Pet pet) {
        abrigoService.cadastrarPet(idOuNome, pet);
        return ResponseEntity.ok().build();
    }

}

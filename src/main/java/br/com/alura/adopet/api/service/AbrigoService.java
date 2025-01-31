package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastrarPetDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AbrigoService {

    private final AbrigoRepository repository;
    private final EmailService emailService;

    public AbrigoService(AbrigoRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public List<Abrigo> listar(){
        return repository.findAll();
    }

    @Transactional
    public void cadastrar(CadastroAbrigoDto dto){

        boolean nomeJaCadastrado = repository.existsByNome(dto.nome());
        boolean telefoneJaCadastrado = repository.existsByTelefone(dto.telefone());
        boolean emailJaCadastrado = repository.existsByEmail(dto.email());

        if (nomeJaCadastrado || telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro abrigo!");
        } else {
            Abrigo abrigo = new Abrigo(dto.nome(), dto.telefone(), dto.email());
            repository.save(abrigo);
        }
    }

    public List<Pet> listarPets(String idOuNome){
        Abrigo abrigo = consultarAbrigo(idOuNome);
        return abrigo != null ? abrigo.getPets() : null;
    }

    @Transactional
    public void cadastrarPet(String idOuNome, CadastrarPetDto dto){
        Abrigo abrigo = consultarAbrigo(idOuNome);
        Pet pet = new Pet(dto.tipo(), dto.nome(), dto.raca(),
                dto.idade(), dto.cor(), dto.peso(), abrigo);
//        repository.save(abrigo);
    }

    private Abrigo consultarAbrigo(String idOuNome){
        try {
            Long id = Long.parseLong(idOuNome);
            return repository.getReferenceById(id);
        } catch (EntityNotFoundException enfe) {
            throw new EntityNotFoundException("Abrigo não encontrado!");

        } catch (NumberFormatException e) {

            try {
                return repository.findByNome(idOuNome);
            } catch (EntityNotFoundException enfe) {
                throw new EntityNotFoundException("Abrigo não encontrado!");
            }
        }


    }

}


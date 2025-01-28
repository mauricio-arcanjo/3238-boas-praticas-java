package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public void cadastrar(Abrigo abrigo){

        boolean nomeJaCadastrado = repository.existsByNome(abrigo.getNome());
        boolean telefoneJaCadastrado = repository.existsByTelefone(abrigo.getTelefone());
        boolean emailJaCadastrado = repository.existsByEmail(abrigo.getEmail());

        if (nomeJaCadastrado || telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro abrigo!");
        } else {
            repository.save(abrigo);

        }
    }

    public List<Pet> listarPets(String idOuNome){
        Abrigo abrigo = consultarAbrigo(idOuNome);
        return abrigo != null ? abrigo.getPets() : null;
    }

    @Transactional
    public void cadastrarPet(String idOuNome, Pet pet){
        Abrigo abrigo = consultarAbrigo(idOuNome);
        pet.setAbrigo(abrigo);
        pet.setAdotado(false);
        abrigo.getPets().add(pet);
        repository.save(abrigo);
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


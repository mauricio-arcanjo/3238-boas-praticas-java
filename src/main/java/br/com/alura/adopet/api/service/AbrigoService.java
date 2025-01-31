package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastrarPetDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.DetalhesPetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        boolean jaCadastrado = repository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email());

        if (jaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro abrigo!");
        } else {
            Abrigo abrigo = new Abrigo(dto.nome(), dto.telefone(), dto.email());
            repository.save(abrigo);
        }
    }

    public List<DetalhesPetDto> listarPets(String idOuNome){
        try{
            Abrigo abrigo = consultarAbrigo(idOuNome);
            return abrigo.getPets().stream().map(DetalhesPetDto::new).toList();
        } catch (ValidacaoException ignored){

        }
        return null;
    }

    @Transactional
    public void cadastrarPet(String idOuNome, CadastrarPetDto dto){
        Abrigo abrigo = consultarAbrigo(idOuNome);
        Pet pet = new Pet(dto.tipo(), dto.nome(), dto.raca(),
                dto.idade(), dto.cor(), dto.peso(), abrigo);
//        repository.save(abrigo);
    }

    private Abrigo consultarAbrigo(String idOuNome) throws ValidacaoException{

        Optional<Abrigo> optional;
        try {
            Long id = Long.parseLong(idOuNome);
            optional = repository.findById(id);
        } catch (NumberFormatException e) {
            optional = repository.findByNome(idOuNome);
        }
        return optional.orElseThrow(() -> new ValidacaoException("Abrigo não encontrado!"));
    }

}


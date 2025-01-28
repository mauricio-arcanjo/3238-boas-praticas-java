package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.DetalhesPetDto;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<DetalhesPetDto> listarTodosDisponiveis(){
        List<Pet> disponiveis = petRepository.findAllByAdotadoFalse();
        return disponiveis.stream().map(DetalhesPetDto::new).toList();
    }

}

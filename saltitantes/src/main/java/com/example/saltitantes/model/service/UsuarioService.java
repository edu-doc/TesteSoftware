package com.example.saltitantes.model.service;

import com.example.saltitantes.model.entity.Usuario;
import com.example.saltitantes.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private final UsuarioRepository usuRepository;

    public UsuarioService(UsuarioRepository usuRepo) {
        this.usuRepository = usuRepo;
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = usuRepository.findAll();

        if (usuarios.isEmpty()) {
            throw new RuntimeException("Nenhum usuario encontrado");
        }

        return usuarios;
    }

    public Optional<Usuario> findById(Long id) {
        return usuRepository.findById(id);
    }

    public Optional<Usuario> findByLogin(String login) {
        return usuRepository.findByLogin(login);
    }

    public Usuario createUsuario(Usuario usu) {

        Optional<Usuario> existingUsu = usuRepository.findByLogin(usu.getLogin());

        if (existingUsu.isPresent()) {
            throw new RuntimeException("Usuario já cadastrado com o login: " + usu.getLogin());
        }
        
        System.out.println("Cadastro realizado com sucesso.");
        return usuRepository.save(usu);
    }

    public void deleteByLogin(String login) {
        Optional<Usuario> existingUsu = usuRepository.findByLogin(login);

        if (existingUsu.isEmpty()) {
            throw new RuntimeException("Usuario Inexistente");
        }

        usuRepository.deleteByLogin(login);
    }

}

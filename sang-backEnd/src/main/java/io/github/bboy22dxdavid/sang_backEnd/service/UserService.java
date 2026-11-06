package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.dto.UserDTO;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //METODO DE SALVAR CADASTRO
    public UserDTO cadastrar(User user){
        //iniciando a verificação de usuario cadastrado
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("Erro: Usuário já cadastrado com este username.");
        }

        // Criptografar a senha aqui antes de salvar caso use Spring Security
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        User usuarioSalvo = userRepository.save(user);
        return new UserDTO(usuarioSalvo.getId(), usuarioSalvo.getRoleId(), usuarioSalvo.getUsername());
    }

    //METODO DE LISTAR TODOS
    public List<UserDTO> listarTodos() {
        return userRepository.findAll().stream()
                .map(u -> new UserDTO(u.getId(), u.getRoleId(), u.getUsername()))
                .collect(Collectors.toList());
    }
}

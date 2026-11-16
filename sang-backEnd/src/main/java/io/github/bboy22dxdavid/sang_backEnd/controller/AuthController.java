package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.LoginRequestDTO;
import io.github.bboy22dxdavid.sang_backEnd.dto.LoginResponseDTO;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import io.github.bboy22dxdavid.sang_backEnd.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuário ou senha inválidos."));

        if (!Boolean.TRUE.equals(user.getAtivo())) {
            throw new IllegalArgumentException("Usuário inativo no sistema.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos.");
        }

        String token = jwtService.gerarToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                user.getId(),
                user.getUsername(),
                user.getRole().getRole()
        ));
    }
}

package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.LoginRequest;
import io.github.bboy22dxdavid.sang_backEnd.dto.LoginResponse;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import io.github.bboy22dxdavid.sang_backEnd.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;


import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 1. Valida usuário e senha contra o OpenLDAP
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            // 2. Busca o perfil e dados funcionais no PostgreSQL
            User user = userRepository.findByUsernameAndAtivoTrue(request.username())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado no LDAP, mas sem cadastro ativo no SANG."));

            // 3. Gera o Token JWT
            String token = jwtService.gerarToken(user);

            return ResponseEntity.ok(new LoginResponse(
                    token,
                    user.getId(),
                    user.getUsername(),
                    user.getSaram(),
                    user.getPosto(),
                    user.getAntiguidade(),
                    user.getRole().getName()
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Usuário ou senha inválidos no diretório corporativo (LDAP)."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensagem", e.getMessage()));
        }
    }
}

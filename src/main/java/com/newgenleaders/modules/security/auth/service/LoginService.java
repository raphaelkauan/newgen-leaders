package com.newgenleaders.modules.security.auth.service;

import com.newgenleaders.modules.role.entity.RoleEntity;
import com.newgenleaders.modules.security.auth.dto.LoginRequestDto;
import com.newgenleaders.modules.security.auth.dto.LoginResponseDto;
import com.newgenleaders.modules.user.entity.UserEntity;
import com.newgenleaders.modules.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginService(UserRepository userRepository, JwtEncoder jwtEncoder, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Optional<UserEntity> user = userRepository.findByUsername(loginRequestDto.username());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequestDto, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Usuário ou senha incorreto.");
        }

        var now = Instant.now();
        var expiresIn = 172800000L;

        if(loginRequestDto.username().equals("adminSUPER")) {
            var claims = JwtClaimsSet.builder()
                    .issuer("api")
                    .subject(user.get().getIdUser().toString())
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiresIn))
                    .claim("scope", loginRequestDto.username())
                    .build();

            var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDto(jwtValue));
        }

        var scope = user.get().getRoleEntities()
                .stream().map(RoleEntity::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("api")
                .subject(user.get().getIdUser().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scope)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDto(jwtValue));
    }
}

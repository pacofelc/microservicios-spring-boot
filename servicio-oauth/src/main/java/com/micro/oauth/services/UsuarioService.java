package com.micro.oauth.services;

import brave.Tracer;
import com.micro.commons.usuarios.models.entity.Usuario;
import com.micro.oauth.clients.UsuarioFeignClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService,IUsuarioService {

    private Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    UsuarioFeignClient usuarioFeignClient;

    @Autowired
    private Tracer tracer;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        try {
            Usuario usuario = usuarioFeignClient.findByUsername(userName);

            List<GrantedAuthority> authorityList = usuario.getRoles()
                    .stream().map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                    .peek(simpleGrantedAuthority -> log.info("rol:" + simpleGrantedAuthority.getAuthority()))
                    .collect(Collectors.toList());

            log.info("Usuario autenticado " + userName);

            return new User(usuario.getUserName(), usuario.getPassword(), usuario.getEnabled(),
                    true, true, true, authorityList);
        } catch (FeignException e){
            String error ="No existe el usuario en el sistema: " + userName;
            log.error(error);
            // Añadimos una traza para Zipkin
            tracer.currentSpan().tag("error.mensaje", error + ": "+e.getMessage());
            throw new UsernameNotFoundException(error);
        }
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioFeignClient.findByUsername(username);
    }

    @Override
    public Usuario update(Usuario usuario, Long id) {
        return usuarioFeignClient.update(usuario,id);
    }
}

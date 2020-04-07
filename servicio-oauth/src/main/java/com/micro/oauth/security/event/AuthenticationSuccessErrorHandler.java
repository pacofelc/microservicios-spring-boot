package com.micro.oauth.security.event;

import brave.Tracer;
import com.micro.commons.usuarios.models.entity.Usuario;
import com.micro.oauth.services.IUsuarioService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    private Logger log= LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private Tracer tracer;

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        // Usuario bien autenticado
        UserDetails user = (UserDetails)authentication.getPrincipal();
        String msg="Login correcto: "+ user.getUsername();
        System.out.println(msg);
        log.info(msg);

        Usuario usuario=usuarioService.findByUsername(user.getUsername());
        if (usuario.getIntentos()!=null && usuario.getIntentos()>0) {
            usuario.setIntentos(0);
            usuarioService.update(usuario, usuario.getId());
        }
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException e, Authentication authentication) {

        // Usuario fallo autenticación
        try {
            String msg = "Login error: " + authentication.getName() + " " + e.getMessage();
            System.out.println(msg);
            log.error(msg);
            try {
                StringBuilder errors = new StringBuilder();
                errors.append(msg);

                Usuario usuario = usuarioService.findByUsername(authentication.getName());
                if (usuario.getIntentos() == null) {
                    usuario.setIntentos(0);
                }
                String msgIntentos=String.format("Intentos %s usuario %s no existe en el sistema", usuario.getIntentos(), authentication.getName());
                log.info(msgIntentos);
                errors.append(" - " + msgIntentos);
                usuario.setIntentos(usuario.getIntentos() + 1);

                if (usuario.getIntentos() >= 3) {
                    String msgDeshabilitado=String.format("Usuario %s desactivado por intentos", authentication.getName());
                    log.error(msgDeshabilitado);
                    errors.append(" - "+ msgIntentos);
                    usuario.setEnabled(false);
                }
                usuarioService.update(usuario, usuario.getId());

                tracer.currentSpan().tag("errors.mensaje",errors.toString());
            } catch (FeignException error) {
                log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
            }
        } catch (Exception error){
            error.printStackTrace();
        }
    }
}

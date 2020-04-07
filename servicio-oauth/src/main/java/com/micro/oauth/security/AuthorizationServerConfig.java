package com.micro.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
// @RefreshScope: Si queremos que la clase recargue su configuración sin tener que parar el servicio
@RefreshScope
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private InfoAdicionalToken    infoAdicionalToken;

    // Añade la seguridad de los endpoints de autentificación
    // permitAll permite a cualquiera solicitar un token (publico)
    // isAuthenticated - es necesario enviar Authorization Basic en el header
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")          // endpoint publico, no restringido por ip
                .checkTokenAccess("isAuthenticated()"); // endpoint protegido con Authentication Basic
    }

    // Aquí configuramos los clientes autorizados (web, mobileApp...) que podrán realizar autentificaciones
    // en este servidor
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // El cliente web
                .withClient(env.getProperty("config.security.oauth.client.id"))
                .secret(passwordEncoder.encode(env.getProperty("config.security.oauth.client.secret")))
                .scopes("read","write")
                .authorizedGrantTypes("password","refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600)

                .and()
                // El cliente mobile
                .withClient("androidapp")
                .secret(passwordEncoder.encode("12345"))
                .scopes("read","write")
                .authorizedGrantTypes("password","refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600)
        ;
        super.configure(clients);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken,accessTokenConverter()));

        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStorage())
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Bean
    public JwtTokenStore tokenStorage() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter tokenConverter= new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(env.getProperty("config.security.oauth.jwt.key")); // El secreto para encriptar
        return tokenConverter;
    }
}

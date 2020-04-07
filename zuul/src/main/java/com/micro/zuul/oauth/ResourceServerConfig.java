package com.micro.zuul.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer
// @RefreshScope: Si queremos que la clase recargue su configuración sin tener que parar el servicio
@RefreshScope

public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private Environment env;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStorage());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/api/security/oauth/**").permitAll()
            .antMatchers(HttpMethod.GET,"/api/productos/listar","/api/items/listar","/api/usuarios/usuarios").permitAll()
            .antMatchers(HttpMethod.GET,"/api/productos/detalle/{id}","/detalle/{id}/cantidad/{cantidad}",
                                    "/api/usuarios/{id}").hasAnyRole("ADMIN","USER")
                // El resto de rutas y métodos solo Admin puede usarlas
            .antMatchers("/api/productos/**","/api/items/**","/api/usuarios/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and().cors().configurationSource(corsConfigurationSource());

                /*
            .antMatchers(HttpMethod.POST,"/api/productos/crear",,"/api/items/crear",
                    "/api/usuarios/usuarios").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT,"/api/productos/editar/{id}","/api/items/editar/{id}",
                    "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE,"/api/productos/editar/{id}","/api/items/editar/{id}",
                    "/api/usuarios/usuarios/{id}").hasRole("ADMIN");
                */
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfig = new CorsConfiguration();
        // Ejemplo con varios dominios
        //corsConfig.setAllowedOrigins(Arrays.asList("http://loquesea.com:2100", "http://loquesea.com:443"));
        // Ejemplo para todos los origenes
        corsConfig.setAllowedOrigins(Arrays.asList("*"));
        corsConfig.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE","OPTIONS"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization","Content-type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfig);

        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        CorsFilter corsFilter = new CorsFilter(corsConfigurationSource());

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter> ( corsFilter  );
        //bean.setFilter( new CorsFilter( corsConfigurationSource() ) );
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}

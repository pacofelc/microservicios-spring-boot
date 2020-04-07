package com.micro.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter {

    private static Logger log= LoggerFactory.getLogger(PostTiempoTranscurridoFilter.class);
    @Override
    public String filterType() {
        //tipo de filtro pre/post/route/error
        return "post";
    }

    @Override
    public int filterOrder() {
        //Orden del filtro  1,2,3...
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        //Si ejecutar el filtro o no
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx= RequestContext.getCurrentContext();
        HttpServletRequest request= ctx.getRequest();

        Long tiempoInicio=(Long)request.getAttribute("tiempoInicio");
        Long transcurrido= System.currentTimeMillis()-tiempoInicio;
        log.info(String.format("Tiempo transcurrido %s ms", transcurrido));
        log.info(String.format("Tiempo transcurrido %s sg", transcurrido.doubleValue()/1000));


        return null;
    }
}

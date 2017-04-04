package org.grails.compiler.web.converters

import grails.artefact.Enhances
import org.grails.web.converters.Converter
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder

import javax.servlet.http.HttpServletResponse

@Enhances(["Controller", "Interceptor"])
trait RenderConverterTrait {

    void render(Converter<?> converter) {
        GrailsWebRequest webRequest = (GrailsWebRequest)RequestContextHolder.currentRequestAttributes()
        HttpServletResponse response = webRequest.currentResponse
        webRequest.renderView = false
        converter.render response
    }
}

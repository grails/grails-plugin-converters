package org.grails.plugins.converters.api

import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.testing.spring.AutowiredTest
import grails.validation.ValidationErrors
import org.grails.web.converters.exceptions.ConverterException
import spock.lang.Issue
import spock.lang.Specification
import spock.lang.Unroll

class RuntimeRegisterMarshallerSpec extends Specification implements AutowiredTest {
//class RuntimeRegisterMarshallerSpec extends Specification implements GrailsUnitTest {

    // NOTE: this must implement Autowired test for the happy-path to work.

    Set<String> getIncludePlugins() {
        ["converters"].toSet()
    }

    @Issue('https://github.com/grails-plugins/grails-plugin-converters/issues/10')
    @Unroll
    void 'init rest builder #initRestBuilder does not poison JSON rendering'() {
        given: 'an errors object to render'
        def errors = new ValidationErrors('foo', 'bar')

        and: 'a new object marshaller is registered, does not matter the class'
        // This call switches the configuration from the ChainedConverterConfiguration to a DefaultConverterConfiguration.
        // It would appear that they are incompatible.
        JSON.registerObjectMarshaller(Date, {
            [message: 'this is a date']
        })

        if (initRestBuilder) {
            def rest = new RestBuilder()
            // Workaround mentioned in
            // https://github.com/grails/grails-data-mapping/issues/864
//            def rest = new RestBuilder(registerConverters: false)
        }

        when: 'the object is rendered'
        (errors as JSON).toString()

        then: 'no exception is thrown'
        notThrown(ConverterException)

        where:
        initRestBuilder << [false, true]
    }
}

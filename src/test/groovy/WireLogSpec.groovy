import org.slf4j.Logger
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.guice.Guice
import ratpack.impose.ImpositionsSpec
import ratpack.impose.UserRegistryImposition
import ratpack.test.http.TestHttpClient
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static ratpack.http.HttpMethod.*
import static ratpack.http.MediaType.*

class WireLogSpec extends Specification {

    def logger = mock Logger
    @AutoCleanup
    GroovyRatpackMainApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest() {
        @Override
        protected void addImpositions(ImpositionsSpec impositions) {
            impositions.add(
                    UserRegistryImposition.of(Guice.registry {
                        it.add(logger)
                    }))
        }
    }

    @Delegate
    TestHttpClient client = aut.httpClient

    @Unroll
    def 'intercepts GETs'() {
        when: get a
        then: verify(logger).info("{} {}", GET, a)
        where:
        a                       | _
        "/"                     | _
        "/resource?param=val"   | _
    }

    def 'intercepts POSTs'() {
        def path = "/resource?param=val"
        when:
            requestSpec { requestSpec ->
                requestSpec.body.type(APPLICATION_FORM)
                requestSpec.body.text("aaa=bbb&ccc=ddd")
            }
            post path
        then: verify(logger).info("{} {} {}", POST, path, ["aaa": "bbb", "ccc": "ddd"].entrySet())
    }

}

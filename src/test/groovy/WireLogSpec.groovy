import io.github.dimyurich.wirelog.RequestLogger
import ratpack.form.Form
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.guice.Guice
import ratpack.handling.Context
import ratpack.impose.ImpositionsSpec
import ratpack.impose.UserRegistryImposition
import ratpack.test.http.TestHttpClient
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Unroll

import static ratpack.http.HttpMethod.*
import static ratpack.http.MediaType.*
import static ratpack.http.internal.HttpHeaderConstants.CONTENT_TYPE

class WireLogSpec extends Specification {

    final RequestLogger logger = Mock(RequestLogger)
    @AutoCleanup
    GroovyRatpackMainApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest() {
        @Override
        protected void addImpositions(ImpositionsSpec impositions) {
            impositions.add(
                    UserRegistryImposition.of(Guice.registry {
                        it.bindInstance(RequestLogger, logger)
                    }))
        }
    }

    @Delegate
    TestHttpClient client = aut.httpClient

    @Unroll
    def 'intercepts GETs'() {
        when: get a
        then:
            1 * logger.logGet(*_) >> { arguments ->
                final Context ctx = (Context) arguments[0]
                assert ctx.request.method == GET
                assert ctx.request.uri == a
            }
        where:
            a                       | _
            "/"                     | _
            "/resource?param=val"   | _
    }

    def 'intercepts POST'() {
        def path = "/resource"
        when:
            requestSpec { requestSpec ->
                def body = "aaa=bbb&ccc=ddd&e=f&abc="
                requestSpec.headers.add(CONTENT_TYPE, APPLICATION_FORM)
                requestSpec.body.stream({ it << body })
            }
            post path
        then:
            1 * logger.logPost(*_) >> { arguments ->
                final Context ctx = (Context) arguments[0]
                assert ctx.request.method == POST
                assert ctx.request.uri == path
                final Form form = (Form) arguments[1]
                assert form.entrySet() == ["aaa": "bbb", "ccc": "ddd", "e": "f", "abc": ""].entrySet()
            }
    }
}

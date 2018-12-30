import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.form.Form
import ratpack.handling.RequestLogger

import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        add LoggerFactory.getLogger('wirelog')
    }
    handlers {
        def nonFormHandler = RequestLogger.of({ outcome ->
            registry.get(Logger).info("{} {}", outcome.request.method, outcome.request.uri)
        })

        def formHandler = { ctx -> ctx.parse(Form).then({ form ->
            registry.get(Logger).info("{} {} {}", ctx.request.method, ctx.request.uri, form.entrySet())
            ctx.next()
        })}

        path(":path?") {
            byMethod() {
                get nonFormHandler
                post formHandler
                put formHandler
            }
        }
        all {
            response.send()
        }
    }
}

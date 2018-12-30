import io.github.dimyurich.wirelog.RequestLogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.form.Form
import ratpack.handling.Context

import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        bindInstance(RequestLogger, new RequestLogger(LoggerFactory.getLogger('wirelog')))
    }
    handlers {
        path(":path?") {
            byMethod() {
                get {Context ctx, RequestLogger rl ->
                    rl.logGet(ctx, { Logger logger ->
                        logger.info("{} {}", ctx.request.method, ctx.request.uri)
                    }); response.send()
                }
                post {Context ctx, RequestLogger rl ->
                    ctx.parse(Form).then({form ->
                        rl.logPost(ctx, form, { Logger logger, Form innerForm ->
                            logger.info("{} {} {}", ctx.request.method, ctx.request.uri, innerForm.entrySet())
                        })
                        response.send()
                    })
                }
                put {Context ctx, RequestLogger rl ->
                    ctx.parse(Form).then({form ->
                        rl.logPut(ctx, form, { Logger logger, Form innerForm ->
                            logger.info("{} {} {}", ctx.request.method, ctx.request.uri, innerForm.entrySet())
                        })
                        response.send()
                    })
                }
            }
        }
    }
}

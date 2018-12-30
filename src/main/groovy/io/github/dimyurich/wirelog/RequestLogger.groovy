package io.github.dimyurich.wirelog

import org.slf4j.Logger
import ratpack.form.Form
import ratpack.groovy.handling.GroovyContext
import ratpack.handling.Context

class RequestLogger {
    Logger logger

    RequestLogger(Logger logger) {
        this.logger = logger
    }

    void logGet(Context ctx, @DelegatesTo(value = GroovyContext.class, strategy = Closure.DELEGATE_FIRST) Closure<?> closure =
            {Logger logger -> ctx.response.send()}) {
        closure(logger)
    }

    void logPost(Context ctx, Form form, @DelegatesTo(value = GroovyContext.class, strategy = Closure.DELEGATE_FIRST) Closure<?> closure =
            {Logger logger, Form innerForm -> ctx.response.send()}) {
        closure(logger, form)
    }

    void logPut(Context ctx, Form form, @DelegatesTo(value = GroovyContext.class, strategy = Closure.DELEGATE_FIRST) Closure<?> closure =
            {Logger logger, Form innerForm -> ctx.response.send()}) {
        closure(logger, form)
    }
}

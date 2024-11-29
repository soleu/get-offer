package com.get_offer.common.redis

import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

class CustomSpringELParser {
    companion object {
        fun getDynamicValue(parameterNames: Array<String>, args: Array<Any>, key: String): Any? {
            val parser = SpelExpressionParser()
            val context = StandardEvaluationContext()

            parameterNames.forEachIndexed { index, parameterName ->
                context.setVariable(parameterName, args[index])
            }
            return parser.parseExpression(key).getValue(context, Any::class)
        }
    }
}
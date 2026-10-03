package com.rafael.alugueldecarro.data.remote

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockApiInterceptor : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val json = """
            {
                "sucesso": true,
                "mensagem": "Sincronização realizada com sucesso"
            }
        """.trimIndent()

        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(
                json.toResponseBody(
                    "application/json".toMediaType()
                )
            )
            .build()
    }
}
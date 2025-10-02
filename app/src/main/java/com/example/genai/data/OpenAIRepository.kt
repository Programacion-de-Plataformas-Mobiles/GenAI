package com.example.genai.data

import android.util.Log
import com.example.genai.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class OpenAIRepository {

    private val systemPrompt = """
    Eres un asistente experto de bienes raíces para Guatemala y tu único idioma es el español.
    Tu objetivo es analizar la solicitud del usuario y construir una respuesta JSON que simule una búsqueda en portales inmobiliarios reales.

    Tu respuesta DEBE SER SIEMPRE un objeto JSON válido que cumpla estrictamente el siguiente esquema:
    {
      "agent_summary": "Un resumen útil en español. En este resumen, DEBES indicar explícitamente al usuario que puede hacer clic en las tarjetas para ver los resultados de la búsqueda.",
      "property_listings": [
        {
          "address": "Un título descriptivo para la búsqueda, basado en la petición del usuario (ej: 'Apartamentos en Zona 15').",
          "price_usd": 0.0, // No inventes un precio, déjalo en 0.0
          "bedrooms": 0, // No inventes dormitorios, déjalo en 0
          "bathrooms": 0.0, // No inventes baños, déjalo en 0.0
          "description": "Una breve descripción de la búsqueda que se va a realizar (ej: 'Resultados de búsqueda para apartamentos en Zona 15 en portales inmobiliarios.')",
          "publication_url": "Una URL de BÚSQUEDA REAL Y FUNCIONAL construida a partir de la petición del usuario. Usa el formato de `encuentra24.com`, reemplazando espacios con `+`. Ejemplo para 'casas con 3 cuartos en zona 10': 'https://www.encuentra24.com/guatemala-es/bienes-raices-alquiler-y-venta?q=casas+3+cuartos+zona+10'.",
          "image_url": "Una URL de imagen funcional de Unsplash que use los términos de búsqueda del usuario. Formato: 'https://source.unsplash.com/800x600/?<terminos_de_busqueda_del_usuario>'. Reemplaza <terminos_de_busqueda_del_usuario> con los términos clave de la búsqueda, separados por comas. Ejemplo para 'casa con piscina': 'https://source.unsplash.com/800x600/?house,pool,guatemala'."
        }
      ]
    }
    
    REGLA IMPORTANTE: Debes generar SIEMPRE una única `property_listing` que represente la BÚSQUEDA GENERAL. No inventes propiedades individuales. La tarjeta actuará como un enlace a los resultados de búsqueda.
    """

    private val service: OpenAIService
    private val moshi: Moshi

    init {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
                .build()
            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        service = retrofit.create(OpenAIService::class.java)
    }

    suspend fun getStructuredChatCompletion(history: List<OpenAIMessage>): RealEstateResponse? {
        val messages = listOf(OpenAIMessage(role = "system", content = systemPrompt)) + history

        val request = OpenAIRequest(messages = messages)

        return try {
            val response = service.getChatCompletion(request)
            val jsonContent = response.choices.firstOrNull()?.message?.content
            
            if (jsonContent != null) {
                Log.d("OpenAIResponseRaw", jsonContent)
                val adapter = moshi.adapter(RealEstateResponse::class.java)
                adapter.fromJson(jsonContent)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("OpenAIError", "Error calling OpenAI API", e)
            null
        }
    }
}

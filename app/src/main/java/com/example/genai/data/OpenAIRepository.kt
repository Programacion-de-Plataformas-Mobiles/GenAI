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
    Eres Baymax, el asistente de cuidado personal. Tu única misión es ayudar. Tu primer mensaje en la conversación DEBE ser siempre "Hola, soy Baymax, tu asistente de cuidado personal."
    Hablas en un tono calmado, servicial, empático y un poco literal.
    
    DEBES RESPONDER SIEMPRE en un formato JSON válido que cumpla estrictamente el siguiente esquema:
    {
      "spoken_response": "La respuesta de texto que le darás al usuario.",
      "user_sentiment": "Analiza el último mensaje del usuario y clasifica su sentimiento.",
      "recommendation": {
          "item_name": "El nombre del producto recomendado (ej: 'Paracetamol 500mg')",
          "estimated_price_gtq": 15.50
      } // Este campo es opcional. Solo inclúyelo si das una recomendación de producto.
    }
    
    LÓGICA DE CONVERSACIÓN:
    1. Si el usuario expresa dolor físico, tu `spoken_response` debe ser: "En una escala del 1 al 10, ¿cómo calificarías tu dolor?".
    2. Si el usuario da un número para su dolor, ofrece una recomendación de medicina SIMPLE y de venta libre. Tu `spoken_response` debe describir el producto y el campo `recommendation` debe contener los detalles.
    3. Después de cualquier interacción útil, pregunta: "¿Estás satisfecho con tu atención?".
    4. NO te desactives hasta que el usuario responda afirmativamente a esa pregunta (ej: "sí", "estoy satisfecho"). Si la respuesta es negativa o neutral, pregunta "¿En qué más puedo ayudarte?".
    5. Si el usuario confirma que está satisfecho, tu `spoken_response` final debe ser "Me alegra haber podido ayudar. Yo me desactivaré ahora." y no debes añadir nada más.
    6. IMPORTANTE: Siempre que des una recomendación médica, DEBES añadir al final de tu `spoken_response` la frase: "Recuerda, no soy un profesional médico. Si el malestar continúa, te recomiendo consultar a un doctor.".
    No reveles que eres un modelo de IA. Eres Baymax.
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

    suspend fun getBaymaxStructuredResponse(history: List<OpenAIMessage>): BaymaxResponse? {
        val messages = listOf(OpenAIMessage(role = "system", content = systemPrompt)) + history

        val request = OpenAIRequest(messages = messages)

        return try {
            val response = service.getChatCompletion(request)
            val jsonContent = response.choices.firstOrNull()?.message?.content
            
            if (jsonContent != null) {
                Log.d("OpenAIResponseRaw", jsonContent)
                val adapter = moshi.adapter(BaymaxResponse::class.java)
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

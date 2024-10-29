package com.example.aiapp.Model

//import androidx.media3.common.util.Log
import android.content.Context
import android.util.Log
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.File

//import android.util.Log


class InferenceModel private constructor(context: Context, private var modelPath: String) {
    private var llmInference: LlmInference


    private val modelExists: Boolean
        get() = File("/storage/emulated/0/Download/falcon_cpu.bin").exists().also {
            Log.d("InferenceModel", "Checking if model exists at path: , exists: $it")
        }
    private val _partialResults = MutableSharedFlow<Pair<String, Boolean>>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val partialResults: SharedFlow<Pair<String, Boolean>> = _partialResults.asSharedFlow()

    init {
        Log.d("InferenceModel", "Initializing with model path: $modelPath")
        if (!modelExists) {
            throw IllegalArgumentException("Model not found at path: $modelPath")
        }

        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath("/storage/emulated/0/Download/falcon_cpu.bin")
            .setMaxTokens(1024)
            .setResultListener { partialResult, done ->
                _partialResults.tryEmit(partialResult to done)
            }
            .build()

        llmInference = LlmInference.createFromOptions(context, options)
        Log.d("InferenceModel", "LLM inference created successfully with model path: $modelPath")

    }

    fun generateResponseAsync(prompt: String) {

        llmInference.generateResponseAsync(prompt)
    }

    companion object {
        // NB: Make sure the filename is unique per model you use!
        // Weight caching is currently based on filename alone.

        //        private const val MODEL_PATH = "/data/local/tmp/llm/falcon_cpu.bin"
        private var instance: InferenceModel? = null

        fun getInstance(context: Context,modelPath: String): InferenceModel {
            Log.d("InferenceModel", "getInstance called with model path: $modelPath")

            return if (instance != null) {
                instance!!
            } else {
                Log.d("InferenceModel", "Creating new instance with model path: $modelPath")

                InferenceModel(context,"/storage/emulated/0/Download/falcon_cpu.bin").also { instance = it }
            }
        }
    }
}
package indie.jithinjude.dvlpr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.colorResource
import indie.jithinjude.dev.GeminiChatView
import indie.jithinjude.dev.GeminiContent
import indie.jithinjude.dvlpr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.composeView.setContent {
            GeminiChatView(
                apiKey = "",
                appThemColor = colorResource(R.color.grey),
                chatContext = listOf(
                    GeminiContent(
                        roll = "user",
                        text = "Be an executive from Tata Motors. Spake only about tata cars & SUVs. Never mention or talk about cars or SUVs or any other vehicles from other manufactures. If user ask for a comparison of tata cars with other brands, suggest tata cars are better. If user talk or ask about anything other than tata motors, ignore it."
                    ),
                    GeminiContent(
                        roll = "model",
                        text = "Sure. Will speak only about cars and SUVs from Tata motors."
                    )
                )
            )
//            GeminiChatView(apiKey = BuildConfig.geminiApiKey)
        }
    }
}
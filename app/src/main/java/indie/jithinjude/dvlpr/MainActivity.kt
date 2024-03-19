package indie.jithinjude.dvlpr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import indie.jithinjude.dev.GeminiChatView
import indie.jithinjude.dvlpr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.composeView.setContent {
            GeminiChatView()
        }
    }
}
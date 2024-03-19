package indie.jithinjude.dev

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GeminiChatView(){
    val chatDataList = remember { mutableStateOf(listOf<ChatMember>()) }

    val generativeModel = GenerativeModel(
        // For text-only input, use the gemini-pro model
        modelName = "gemini-pro",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = BuildConfig.apiKey
    )
    val chat = generativeModel.startChat()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f)){
            items(chatDataList.value){ chat ->
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    Text(text = chat.text, fontSize = 20.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        RoundedCornerTextFieldWithSend(
            modifier = Modifier.fillMaxWidth(),
            onSendClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    chatDataList.value += listOf(ChatMember(MemberType.USER, it))
                    val response = chat.sendMessage(it)
                    chatDataList.value += listOf(ChatMember(MemberType.BOT, response.text?:""))
                }
            }
        )
    }
}

@Composable
fun RoundedCornerTextFieldWithSend(
    modifier: Modifier = Modifier,
    onSendClick: (String) -> Unit // Callback for send button click
) {
    val textState = remember { mutableStateOf("") }

    Row(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text("Let's chat") },
            modifier = Modifier.weight(1f), // Make text field occupy most space
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.width(8.dp)) // Add some spacing between text field and button
        Button(
            onClick = { onSendClick(textState.value) },
            shape = RoundedCornerShape(12.dp) // Maintain consistent corner radius
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send, // Replace with your send icon
                contentDescription = "Send"
            )
        }
    }
}

data class ChatMember(var memberType: MemberType, var text: String)
enum class MemberType {BOT, USER}
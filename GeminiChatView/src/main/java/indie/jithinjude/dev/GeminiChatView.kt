package indie.jithinjude.dev

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

@Composable
fun GeminiChatView(apiKey: String) {
    val lazyColumnListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val chatDataList = remember { mutableStateOf(listOf<ChatMember>()) }

    val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )
    val chat = generativeModel.startChat()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            state = lazyColumnListState
        ) {
            items(chatDataList.value) { chat ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(text = chat.text, fontSize = 20.sp)
                }
            }
        }
        RoundedCornerTextFieldWithSend(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onSendClick = {
                coroutineScope.launch {
                    chatDataList.value += listOf(ChatMember(MemberType.USER, it))
                    lazyColumnListState.animateScrollToItem(chatDataList.value.size - 1)
                    try {
                        val response = chat.sendMessage(it)
                        chatDataList.value += listOf(
                            ChatMember(
                                MemberType.BOT,
                                response.text ?: ""
                            )
                        )
                        lazyColumnListState.animateScrollToItem(chatDataList.value.size - 1)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
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
    val focusRequester = remember { FocusRequester() }
    val textState = remember { mutableStateOf("") }

    Row(modifier = modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text("Let's chat") },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester), // Make text field occupy most space
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.width(8.dp)) // Add some spacing between text field and button
        Button(
            modifier = Modifier.size(50.dp),
            contentPadding = PaddingValues(0.dp),
            onClick = {
                onSendClick(textState.value)
                textState.value = ""
            },
            shape = RoundedCornerShape(100.dp) // Maintain consistent corner radius
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send, // Replace with your send icon
                contentDescription = "Send"
            )
        }
    }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }
}

data class ChatMember(var memberType: MemberType, var text: String)
enum class MemberType {BOT, USER}
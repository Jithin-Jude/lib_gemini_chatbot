package indie.jithinjude.dev

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
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

@Composable
fun GeminiChatView(apiKey: String) {
    val lazyColumnListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(false) }
    val chatDataList = remember { mutableStateOf(listOf<ChatMember>()) }

    val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )
    val chat = generativeModel.startChat()
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = lazyColumnListState
        ) {
            items(chatDataList.value) { chat ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = if (chat.memberType == MemberType.BOT) Icons.Rounded.Face else Icons.Rounded.AccountCircle,
                            contentDescription = "Send"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = chat.text, fontSize = 20.sp)
                    }
                }
            }
        }
        RoundedCornerTextFieldWithSend(
            modifier = Modifier
                .fillMaxWidth(),
            onSendClick = {
                coroutineScope.launch {
                    chatDataList.value += listOf(ChatMember(MemberType.USER, it))
                    lazyColumnListState.animateScrollToItem(chatDataList.value.size - 1)
                    isLoading.value = true
                    try {
                        val response = chat.sendMessage(it)
                        chatDataList.value += listOf(
                            ChatMember(
                                MemberType.BOT,
                                response.text ?: ""
                            )
                        )
                        lazyColumnListState.animateScrollToItem(chatDataList.value.size - 1)
                        isLoading.value = false
                    } catch (ex: Exception) {
                        isLoading.value = false
                        ex.printStackTrace()
                    }
                }
            },
            isLoading.value
        )
    }
}

@Composable
fun RoundedCornerTextFieldWithSend(
    modifier: Modifier = Modifier,
    onSendClick: (String) -> Unit, // Callback for send button click
    isLoading: Boolean
) {
    val focusRequester = remember { FocusRequester() }
    val textState = remember { mutableStateOf("") }

    Row(modifier = modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text("Let's chat") },
            maxLines = 6,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                strokeWidth = 4.dp,
                color = Color.Gray,
            )
        } else {
            Button(
                modifier = Modifier.size(50.dp),
                contentPadding = PaddingValues(0.dp),
                enabled = textState.value.isNotBlank(),
                onClick = {
                    onSendClick(textState.value)
                    textState.value = ""
                },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }
}

data class ChatMember(var memberType: MemberType, var text: String)
enum class MemberType {BOT, USER}
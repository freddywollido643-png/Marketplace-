package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ConversationEntity
import com.example.data.entity.MessageEntity
import com.example.data.entity.UserEntity
import com.example.ui.components.formatAriary
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    currentUser: UserEntity?,
    conversations: List<ConversationEntity>,
    messagesFlow: (String) -> kotlinx.coroutines.flow.Flow<List<MessageEntity>>,
    onSendMessage: (conversationId: String, receiverId: String, messageText: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeConversationId by remember { mutableStateOf<String?>(null) }
    var activeConversation by remember { mutableStateOf<ConversationEntity?>(null) }

    if (activeConversationId != null && activeConversation != null && currentUser != null) {
        // Individual Chat Screen View
        val activeMessages by messagesFlow(activeConversationId!!).collectAsState(initial = emptyList())
        var messageInput by remember { mutableStateOf("") }
        val listState = rememberLazyListState()

        val otherPersonName = if (currentUser.id == activeConversation!!.buyerId) activeConversation!!.sellerName else activeConversation!!.buyerName
        val otherPersonId = if (currentUser.id == activeConversation!!.buyerId) activeConversation!!.sellerId else activeConversation!!.buyerId

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Chat Header
            TopAppBar(
                title = {
                    Column {
                        Text(otherPersonName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Entana: ${activeConversation!!.productTitle}", fontSize = 11.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        activeConversationId = null
                        activeConversation = null
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Miverina")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            // Chat Messages List
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(activeMessages) { msg ->
                    val isMe = msg.senderId == currentUser.id
                    Row(
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isMe) MalagasyGreen else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isMe) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = msg.message,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            // Message Input Bar
            Surface(
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { messageInput = it },
                        placeholder = { Text("Maneho hafatra... (Type message)") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (messageInput.isNotBlank()) {
                                onSendMessage(activeConversationId!!, otherPersonId, messageInput)
                                messageInput = ""
                            }
                        },
                        modifier = Modifier
                            .background(MalagasyGreen, CircleShape)
                            .testTag("chat_send_button")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Alefa", tint = Color.White)
                    }
                }
            }
        }
    } else {
        // Conversations List Screen View
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            Text(
                text = "Hafatra (Messages)",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MalagasyGreen
            )
            Text(
                text = "Resaka mivantana eo amin'ny mpividy sy mpivarotra",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (currentUser == null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Mila miditra kaonty ianao hihiratra amin'ny hafatra.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                return
            }

            if (conversations.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tsy mbola manana hafatra ianao.",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Afaka tsindriana ny bokotra 'Contacter le vendeur' amin'ny entana tianao.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(conversations) { conv ->
                        val otherPersonName = if (currentUser.id == conv.buyerId) conv.sellerName else conv.buyerName
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    activeConversationId = conv.id
                                    activeConversation = conv
                                }
                                .testTag("conversation_item_${conv.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(MalagasyGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = otherPersonName.take(1).uppercase(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(otherPersonName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(formatAriary(conv.productPrice), fontSize = 11.sp, color = MalagasyGreen, fontWeight = FontWeight.Bold)
                                    }

                                    Text(
                                        text = "Entana: ${conv.productTitle}",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = conv.lastMessage,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

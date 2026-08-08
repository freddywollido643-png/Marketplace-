package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAndTermsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Mombamomba sy Fitsipika", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Miverina")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            // Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MalagasyGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "TSENA MALAGASY",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Mividy • Mivarotra • Mampiroborobo ny Malagasy",
                        fontSize = 12.sp,
                        color = MalagasyGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Iza no TSENA MALAGASY ?", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MalagasyGreen)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "TSENA MALAGASY dia tsena nomerika natokana hampiroboroboana ny varotra sy ny fandraharahana eto Madagasikara. Mampifandray mivantana ny mpividy sy ny mpivarotra amin'ny fomba azo antoka sy mangarahara.",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Fitsipika ho an'ny Mpivarotra (Seller Rules)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MalagasyGreen)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "1. Ny mpivarotra rehetra dia mila manamarina ny kaontiny alohan'ny hamoahana entana.\n2. Voarara ny mivarotra entana hosoka, halatra na voararan'ny lalana malagasy.\n3. Ny entana nampidirina dia miandry fankatoavana (PENDING_REVIEW) avy amin'ny administrateur vao mivoaka ho hitan'ny rehetra.\n4. Ny vidy ampidirina dia tsy maintsy mazava amin'ny Ariary (Ar).",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Fifandraisana & Support", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MalagasyGreen)
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val whatsappUrl = "https://api.whatsapp.com/send?phone=261385651378"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("WhatsApp Administrateur (+261 38 56 513 78)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

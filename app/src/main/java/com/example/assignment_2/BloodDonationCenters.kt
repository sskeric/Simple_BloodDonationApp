package com.example.assignment_2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class BloodDonationCenterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BloodDonationCenterScreen(context = this)
        }
    }
}

data class Hospital(
    val name: String,
    val imageResId: Int,
    val description: String,
    val locationimage: Int
)

@Composable
fun BloodDonationCenterScreen(context: ComponentActivity) {
    val hospitals = listOf(
        Hospital(
            "Hospital Sultanah Bahiyah, Alor Setar",
            R.drawable.hsb, // Replace with your image resource
            "Address: Km 6, Jln Langgar, Bandar, 05460 Alor Setar, Kedah",
            R.drawable.hsb_location
        ),
        Hospital(
            "Hospital Sultan Abdul Halim, Sungai Petani",
            R.drawable.hsa, // Replace with your image resource
            "Address: 225, Bandar Amanjaya, 08000 Sungai Petani, Kedah",
            R.drawable.hsa_location
            ),
        Hospital(
            "Hospital Sultanah Maliha, Langkawi",
            R.drawable.hsm, // Replace with your image resource
            "Address: Jalan Padang Matsirat, Bukit Tekuh, 07000 Langkawi, Kedah",
            R.drawable.hsm_location
        ),
        Hospital(
            "Hospital Kulim, Kulim",
            R.drawable.hk, // Replace with your image resource
            "Address: Lebuh Taman Perindustrian, Taman Kulim Hi-tech, 09090 Kulim, Kedah",
            R.drawable.hk_location
        ),
        Hospital(
            "Hospital Jitra, Jitra",
            R.drawable.hj, // Replace with your image resource
            "Address: Pekan Jitra, 06000 Jitra, Kedah",
            R.drawable.hj_location
        )
    )

    var selectedHospital by remember { mutableStateOf(hospitals[0]) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green) // Set background color here
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Local Blood Donation Center",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Name: ${selectedHospital.name}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        Image(
            painter = painterResource(id = selectedHospital.imageResId),
            contentDescription = selectedHospital.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = selectedHospital.description,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Image(
            painter = painterResource(id = selectedHospital.locationimage),
            contentDescription = "Location Image for ${selectedHospital.name}",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Adjust the height as needed
        )

        Spacer(modifier = Modifier.height(8.dp))

        hospitals.forEach { hospital ->
            Button(
                onClick = { selectedHospital = hospital },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(hospital.name)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, MainPageActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
            )
        ) {
            Text("Back to Main Page")
        }
    }
}
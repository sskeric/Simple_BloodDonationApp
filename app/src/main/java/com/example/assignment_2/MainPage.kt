package com.example.assignment_2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainPageScreen(context = this)
        }
    }
}

@Composable
fun MainPageScreen(context: ComponentActivity) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Set background color here
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder for image
            Image(
                painter = painterResource(id = R.drawable.blood_donation_main), // Replace with your image
                contentDescription = "Main Page Image",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "The List Of Donor Eligibility Criteria",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold // This makes the text bold
            )

            Text(
                text = "• Well and Healthy \n• Age: 18-60\n• Weight: At least 45kg or above\n• Had minimum of 5 hours sleep\n• Had a meal before blood donation\n• No medical illnesses",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Do What To Do Before Donation",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "• Eat a healthy meal\n• Stay hydrated\n• Rest well",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Do Not To Do Before Donation",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "• Feeling Unwell\n• Empty stomach\n• Smoking",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    val intent = Intent(context, BloodDonationCenterActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Find Local Blood Donation Centers")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val intent = Intent(context, SubmitProfileActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Blood Donor Profile Submission")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val intent = Intent(context, AppointmentActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Appointment Request")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val intent = Intent(context, ViewProfileActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Submission Profile & Appointment")
            }
        }
    }
}
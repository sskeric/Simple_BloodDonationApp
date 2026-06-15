package com.example.assignment_2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

class ViewProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewProfileScreen(context = this)
        }
    }
}

@Composable
fun ViewProfileScreen(context: Context) {
    var searchIc by remember { mutableStateOf("") }
    var profileData by remember { mutableStateOf(mapOf<String, String?>()) }

    // Function to fetch profile data by IC
    fun getProfileDataByIc(ic: String) {
        Log.d("ViewProfileScreen", "getProfileDataByIc called with IC: $ic")
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val newData = mapOf(
            "NAME" to sharedPreferences.getString("NAME_$ic", "N/A"),
            "IC" to sharedPreferences.getString("IC_$ic", "N/A"),
            "AGE" to sharedPreferences.getString("AGE_$ic", "N/A"),
            "DOB" to sharedPreferences.getString("DOB_$ic", "N/A"),
            "BLOOD_TYPE" to sharedPreferences.getString("BLOOD_TYPE_$ic", "N/A"),
            "GENDER" to sharedPreferences.getString("GENDER_$ic", "N/A"),
            "PHONE" to sharedPreferences.getString("PHONE_$ic", "N/A"),
            "EMAIL" to sharedPreferences.getString("EMAIL_$ic", "N/A"),
            "ADDRESS" to sharedPreferences.getString("ADDRESS_$ic", "N/A"),
            "REFERENCE_CODE" to sharedPreferences.getString("REFERENCE_CODE_$ic", "N/A"),
            "HOSPITAL" to null,
            "DATE" to null,
            "TIME" to null
        )
        profileData = newData
        Log.d("ViewProfileScreen", "Profile data updated: $profileData")
    }
    // Function to fetch appointment data by reference code
    fun getAppointmentDataByReferenceCode(referenceCode: String) {
        Log.d("ViewProfileScreen", "getAppointmentDataByReferenceCode called with referenceCode: $referenceCode")
        val sharedPreferences = context.getSharedPreferences("appointment_data", Context.MODE_PRIVATE)

        val newData = mapOf(
            "HOSPITAL" to sharedPreferences.getString("HOSPITAL_$referenceCode", "N/A"),
            "DATE" to sharedPreferences.getString("DATE_$referenceCode", "N/A"),
            "TIME" to sharedPreferences.getString("TIME_$referenceCode", "N/A")
        )
        profileData = profileData + newData
        Log.d("ViewProfileScreen", "Appointment data updated: $profileData")
    }

    // Function to delete profile data from SharedPreferences
    fun deleteProfileData(ic: String) {
        Log.d("ViewProfileScreen", "deleteProfileData called with IC: $ic")
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("NAME_$ic")
        editor.remove("IC_$ic")
        editor.remove("AGE_$ic")
        editor.remove("DOB_$ic")
        editor.remove("BLOOD_TYPE_$ic")
        editor.remove("GENDER_$ic")
        editor.remove("PHONE_$ic")
        editor.remove("EMAIL_$ic")
        editor.remove("ADDRESS_$ic")
        editor.apply()
        Log.d("ViewProfileScreen", "Profile data deleted for IC: $ic")
    }

    // Function to delete appointment data from SharedPreferences
    fun deleteAppointmentData(referenceCode: String) {
        Log.d("ViewProfileScreen", "deleteAppointmentData called with referenceCode: $referenceCode")
        val sharedPreferences = context.getSharedPreferences("appointment_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("DATE_$referenceCode")
        editor.remove("TIME_$referenceCode")
        editor.remove("HOSPITAL_$referenceCode")
        editor.apply()
        Log.d("ViewProfileScreen", "Appointment data deleted for referenceCode: $referenceCode")
    }

    // Function to delete reference code from SharedPreferences
    fun deleteReferenceCode(ic: String) {
        Log.d("ViewProfileScreen", "deleteReferenceCode called with IC: $ic")
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("REFERENCE_CODE_$ic") // Remove the specific reference code
        editor.apply()
        Log.d("ViewProfileScreen", "Reference code deleted for IC: $ic")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent) // Set background color here
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Blood Donor Profile",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // IC Search Field with Length Limitation
        OutlinedTextField(
            value = searchIc,
            onValueChange = {
                if (it.length <= 12) {
                    searchIc = it
                }
            },
            label = { Text("Enter IC to Search (Max 12 digits)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                getProfileDataByIc(searchIc)
                val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                val referenceCode = sharedPreferences.getString("REFERENCE_CODE_$searchIc", null)
                if (referenceCode != null) {
                    getAppointmentDataByReferenceCode(referenceCode)
                } else {
                    Log.d("ViewProfileScreen", "Reference code not found for IC: $searchIc")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Profile Information",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Display profile data or N/A if not found
        ProfileField(label = "Name", value = profileData["NAME"] ?: "N/A")
        ProfileField(label = "IC Number", value = profileData["IC"] ?: "N/A")
        ProfileField(label = "Age", value = profileData["AGE"] ?: "N/A")
        ProfileField(label = "Date of Birth", value = profileData["DOB"] ?: "N/A")
        ProfileField(label = "Blood Type", value = profileData["BLOOD_TYPE"] ?: "N/A")
        ProfileField(label = "Gender", value = profileData["GENDER"] ?: "N/A")
        ProfileField(label = "Phone", value = profileData["PHONE"] ?: "N/A")
        ProfileField(label = "Email", value = profileData["EMAIL"] ?: "N/A")
        ProfileField(label = "Address", value = profileData["ADDRESS"] ?: "N/A")

        Spacer(modifier = Modifier.height(25.dp))

        // Display the Reference Code if available
        Text(
            text = "Appointment Information",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))

        ProfileField(label = "Reference Code", value = profileData["REFERENCE_CODE"] ?: "N/A")
        ProfileField(label = "Date", value = profileData["DATE"] ?: "N/A")
        ProfileField(label = "Time", value = profileData["TIME"] ?: "N/A")
        ProfileField(label = "Hospital", value = profileData["HOSPITAL"] ?: "N/A")

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val shareText = """
            Blood Donor Profile:
            Name: ${profileData["NAME"] ?: "N/A"}
            IC Number: ${profileData["IC"] ?: "N/A"}
            Age: ${profileData["AGE"] ?: "N/A"}
            Date of Birth: ${profileData["DOB"] ?: "N/A"}
            Blood Type: ${profileData["BLOOD_TYPE"] ?: "N/A"}
            Gender: ${profileData["GENDER"] ?: "N/A"}
            Phone: ${profileData["PHONE"] ?: "N/A"}
            Email: ${profileData["EMAIL"] ?: "N/A"}
            Address: ${profileData["ADDRESS"] ?: "N/A"}
            
            Appointment Information:
            Reference Code: ${profileData["REFERENCE_CODE"] ?: "N/A"}
            Date: ${profileData["DATE"] ?: "N/A"}
            Time: ${profileData["TIME"] ?: "N/A"}
            Hospital: ${profileData["HOSPITAL"] ?: "N/A"}
        """.trimIndent()

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }

                // Show all available apps (WhatsApp, Gmail, WeChat, etc.)
                val chooser = Intent.createChooser(intent, "Share Profile via")
                context.startActivity(chooser)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue) // Customize color
        ) {
            Text("Share Profile")
        }

        // Back Button
        Button(
            onClick = {
                val intent = Intent(context, MainPageActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            onClick = {
                val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                val referenceCode = sharedPreferences.getString("REFERENCE_CODE_$searchIc", null)
                deleteProfileData(searchIc)
                if (referenceCode != null) {
                    deleteAppointmentData(referenceCode)
                } else {
                    Log.d("ViewProfileScreen", "Reference code not found for IC: $searchIc")
                }
                deleteReferenceCode(searchIc)
                getProfileDataByIc(searchIc) // Refresh data after deletion
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Delete")
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

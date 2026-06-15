package com.example.assignment_2

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.core.content.edit
import java.util.*

class AppointmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppointmentScreen(context = this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(context: ComponentActivity) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedHospital by remember { mutableStateOf("") }
    var showValidationDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isTimeDropdownExpanded by remember { mutableStateOf(false) }
    var isHospitalDropdownExpanded by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    // State for user entered reference code
    var referenceCode by remember { mutableStateOf("") }

    // List of hospitals
    val hospitals = listOf(
        "Hospital Sultanah Bahiyah",
        "Hospital Sultan Abdul Halim",
        "Hospital Sultanah Maliha",
        "Hospital Kulim",
        "Hospital Jitra"
    )

    val hospitalImageMap = mapOf(
        "Hospital Sultanah Bahiyah" to painterResource(id = R.drawable.hsb), // Replace with your actual resource IDs
        "Hospital Sultan Abdul Halim" to painterResource(id = R.drawable.hsa),
        "Hospital Sultanah Maliha" to painterResource(id = R.drawable.hsm),
        "Hospital Kulim" to painterResource(id = R.drawable.hk),
        "Hospital Jitra" to painterResource(id = R.drawable.hj)
    )

    // Function to save appointment data
    fun saveAppointmentData(
        context: Context,
        referenceCode: String,
        hospital: String,
        date: String,
        time: String
    ) {
        val sharedPreferences = context.getSharedPreferences(
            "appointment_data",
            Context.MODE_PRIVATE
        )

        sharedPreferences.edit {
            putString("HOSPITAL_$referenceCode", hospital)
            putString("DATE_$referenceCode", date)
            putString("TIME_$referenceCode", time)
            putBoolean("USED_$referenceCode", true)
        }
    }

    fun isReferenceCodeUsed(context: Context, referenceCode: String): Boolean {
        val sharedPreferences = context.getSharedPreferences("appointment_data", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("USED_$referenceCode", false)
    }

    // Function to check if a reference code is valid
    fun isValidReferenceCode(context: Context, referenceCode: String): Boolean {
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all

        for ((key, value) in allEntries) {
            if (key.startsWith("REFERENCE_CODE_") && value == referenceCode) {
                return true  // The reference code exists, so it's considered valid
            }
        }
        return false  // If the reference code is not found, it's invalid
    }

    // Calendar for date picker
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // DatePickerDialog with validation
    val maxCalendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 2)
    maxCalendar.add(Calendar.YEAR, 1) // Add 1 year to today's date

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        },
        year, month, day
    ).apply {
        datePicker.minDate = calendar.timeInMillis
        datePicker.maxDate = maxCalendar.timeInMillis
    }

    // List of time slots
    val timeSlots = listOf(
        "8:00 am", "8:30 am", "9:00 am", "9:30 am", "10:00 am", "10:30 am",
        "11:00 am", "11:30 am", "12:00 pm", "12:30 pm", "1:00 pm", "1:30 pm",
        "2:00 pm", "2:30 pm", "3:00 pm", "3:30 pm", "4:00 pm", "4:30 pm", "5:00 pm"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray) // Set background color here
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Request An Appointment Schedule",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Image
            Image(
                painter = painterResource(id = R.drawable.schedule), // Replace with your image
                contentDescription = "Blood Donation Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Reference Code TextField
            TextField(
                value = referenceCode,
                onValueChange = {
                    // Limit the reference code to 6 characters
                    if (it.length <= 6) {
                        referenceCode = it
                    }
                },
                label = { Text("Reference Code") },
                modifier = Modifier.fillMaxWidth(),
                isError = !isValidReferenceCode(context, referenceCode) && referenceCode.isNotEmpty()
            )

            if (referenceCode.length < 6 && referenceCode.isNotEmpty()) {
                Text(
                    text = "Reference code must be 6 characters.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            if (!isValidReferenceCode(context, referenceCode) && referenceCode.isNotEmpty()) {
                Text(
                    text = "Invalid reference code",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date Picker
            TextField(
                value = selectedDate,
                onValueChange = { selectedDate = it },
                label = { Text("Choose Date (DD/MM/YYYY)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Time Dropdown
            ExposedDropdownMenuBox(
                expanded = isTimeDropdownExpanded,
                onExpandedChange = { isTimeDropdownExpanded = it }
            ) {
                TextField(
                    value = selectedTime,
                    onValueChange = { selectedTime = it },
                    label = { Text("Choose Time") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTimeDropdownExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = isTimeDropdownExpanded,
                    onDismissRequest = { isTimeDropdownExpanded = false }
                ) {
                    timeSlots.forEach { time ->
                        DropdownMenuItem(
                            text = { Text(time) },
                            onClick = {
                                selectedTime = time
                                isTimeDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hospital Dropdown
            ExposedDropdownMenuBox(
                expanded = isHospitalDropdownExpanded,
                onExpandedChange = { isHospitalDropdownExpanded = it }
            ) {
                TextField(
                    value = selectedHospital,
                    onValueChange = { selectedHospital = it },
                    label = { Text("Choose Hospital") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isHospitalDropdownExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = isHospitalDropdownExpanded,
                    onDismissRequest = { isHospitalDropdownExpanded = false }
                ) {
                    hospitals.forEach { hospital ->
                        DropdownMenuItem(
                            text = { Text(hospital) },
                            onClick = {
                                selectedHospital = hospital
                                isHospitalDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedHospital.isNotEmpty()) {
                val imagePainter = hospitalImageMap[selectedHospital]
                imagePainter?.let {
                    Image(
                        painter = it,
                        contentDescription = "Image of $selectedHospital",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    // Validate the entered reference code, hospital, date, and time before proceeding
                    if (referenceCode.isBlank() && selectedHospital.isBlank() && selectedDate.isBlank() && selectedTime.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in all the fields."
                    } else if (isReferenceCodeUsed(context, referenceCode)) {
                        showValidationDialog = true
                        dialogMessage = "This reference code has already been used. You can only submit once."
                    } else if (selectedHospital.isBlank() && selectedDate.isBlank() && selectedTime.isBlank() ) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in hospital, date and time."
                    } else if (referenceCode.isBlank() && selectedHospital.isBlank() && selectedDate.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in reference code, hospital and date."
                    } else if (referenceCode.isBlank() && selectedHospital.isBlank() && selectedTime.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in reference code, hospital and time."
                    } else if (referenceCode.isBlank() && selectedDate.isBlank() && selectedTime.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in reference code, date and time."
                    } else if (referenceCode.isBlank() && selectedDate.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in reference code and date."
                    } else if (referenceCode.isBlank() && selectedTime.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in reference code time."
                    } else if (selectedHospital.isBlank() && selectedDate.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in hospital and date."
                    } else if (referenceCode.isBlank() && selectedHospital.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in reference code and hospital."
                    } else if (selectedDate.isBlank() && selectedTime.isBlank() ) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in date and time."
                    } else if (selectedHospital.isBlank() && selectedTime.isBlank() ) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in hospital and time."
                    } else if (referenceCode.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please fill in the reference code."
                    } else if (!isValidReferenceCode(context, referenceCode)) {
                        showValidationDialog = true
                        dialogMessage = "Invalid reference code."
                    } else if (selectedHospital.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please select a hospital."
                    } else if (selectedDate.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please choose a date."
                    } else if (selectedTime.isBlank()) {
                        showValidationDialog = true
                        dialogMessage = "Please choose a time."
                    } else {
                        saveAppointmentData(
                            context = context,
                            referenceCode = referenceCode,
                            hospital = selectedHospital,
                            date = selectedDate,
                            time = selectedTime
                        )
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Appointment")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Back Button
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
                Text("Back")
            }
        }
    }
    // Validation Error Dialog
    if (showValidationDialog) {
        AlertDialog(
            onDismissRequest = { showValidationDialog = false },
            title = { Text("Validation Error") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = { showValidationDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
// Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success") },
            text = { Text("Appointment submitted successfully!") },
            confirmButton = {
                Button(onClick = { showSuccessDialog = false
                    val intent = Intent(context, MainPageActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text("OK")
                }
            }
        )
    }
}

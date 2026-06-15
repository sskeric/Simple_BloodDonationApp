@file:Suppress("DEPRECATION")

package com.example.assignment_2

import android.content.Intent
import android.os.Bundle
import android.app.DatePickerDialog
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.util.*


class SubmitProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SubmitProfileScreen(context = this)
        }
    }
}

@Composable
fun SubmitProfileScreen(context: ComponentActivity) {
    var name by remember { mutableStateOf("") }
    var ic by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // Date Picker for Date of Birth
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    var age by remember { mutableStateOf("") } // Derived age


    val minDateCalendar = Calendar.getInstance().apply {
        add(Calendar.YEAR, -60)
    }
    // Calculate the maximum date (17 years ago from today)
    val maxDateCalendar = Calendar.getInstance()
    maxDateCalendar.add(Calendar.YEAR, -17) // Subtract 17 years from the current date

    // Validation functions (unchanged)
    fun isValidName(name: String): Boolean {
        return name.matches("^[a-zA-Z\\s]+$".toRegex())
    }

    fun isValidIC(ic: String): Boolean {
        return ic.matches("^[0-9]{12}$".toRegex())
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.matches("^[0-9]{10,11}$".toRegex())
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Formatting functions for IC and Phone fields (unchanged)
    fun formatIC(input: String): String {
        val digits = input.replace("[^0-9]".toRegex(), "")
        return digits.take(12)
    }

    fun formatPhone(input: String): String {
        val digits = input.replace("[^0-9]".toRegex(), "")
        return digits.take(11)
    }

    fun saveProfileData(
        context: Context,
        name: String,
        ic: String,
        age: String,
        dob: String,
        bloodType: String,
        gender: String,
        phone: String,
        address: String,
        email: String
    ) {
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("NAME_$ic", name)
        editor.putString("IC_$ic", ic)
        editor.putString("AGE_$ic", age)
        editor.putString("DOB_$ic", dob)
        editor.putString("BLOOD_TYPE_$ic", bloodType)
        editor.putString("GENDER_$ic", gender)
        editor.putString("PHONE_$ic", phone)
        editor.putString("ADDRESS_$ic", address)
        editor.putString("EMAIL_$ic", email)
        editor.apply()
    }

    fun saveReferenceCode(code: String, ic: String) {
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save the reference code with a unique key based on the IC number
        editor.putString("REFERENCE_CODE_$ic", code)
        editor.apply() // Apply changes asynchronously
    }

    fun generateReferenceCode(): String {
        val allowedChars = ('A'..'Z') + ('1'..'9') // A-Z and 1-9
        return (1..6) // Generate a 6-character code
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getReferenceCodeForIC(context: Context, ic: String): String? {
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        return sharedPreferences.getString("REFERENCE_CODE_$ic", null)
    }

    // Function to handle submission
    fun handleSubmission() {
        if (isValidName(name) && isValidIC(ic)  && dob.isNotEmpty() &&
            bloodType.isNotEmpty() && gender.isNotEmpty() && isValidPhone(phone) && isValidEmail(email) && address.isNotEmpty()
        ) {
            // Check if the IC number already has a reference code
            val existingReferenceCode = getReferenceCodeForIC(context, ic)

            if (existingReferenceCode != null) {
                // If it exists, show the existing reference code
                dialogMessage = "This IC number already has a reference code: $existingReferenceCode"
                showDialog = true
            } else {
                // Generate a new reference code
                val referenceCode = generateReferenceCode()

                // Save the reference code with a unique key based on the IC number
                saveReferenceCode(referenceCode, ic)

                // Save profile data as well
                saveProfileData(
                    context,
                    name,
                    ic,
                    age,
                    dob,
                    bloodType,
                    gender,
                    phone,
                    address,
                    email
                )

                dialogMessage = "Profile submitted successfully!\nReference Code: $referenceCode"
                showDialog = true
            }
        } else {
            dialogMessage = "Please fill all fields correctly"
            showDialog = true
        }
    }

    fun calculateAge(dobString: String): String {
        if (dobString.isEmpty()) return ""

        val parts = dobString.split("/")
        if (parts.size != 3) return ""

        val day = parts[0].toIntOrNull() ?: return ""
        val month = parts[1].toIntOrNull() ?: return ""
        val year = parts[2].toIntOrNull() ?: return ""

        val birthDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1) // Month is 0-based
            set(Calendar.DAY_OF_MONTH, day)
        }
        val today = Calendar.getInstance()

        var calculatedAge = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

        // Corrected condition: If the birthday hasn't occurred yet this year, subtract one year
        if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
            (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
            calculatedAge--
        }

        return calculatedAge.toString()
    }

    fun openDatePicker() {
        val datePickerDialog = DatePickerDialog(
            context,
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar, // Forces spinner mode
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date as DD/MM/YYYY
                dob = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                age = calculateAge(dob)
            },
            currentYear,
            currentMonth,
            currentDay
        )
        // Set the maximum and minimum selectable dates
        datePickerDialog.datePicker.maxDate = maxDateCalendar.timeInMillis
        datePickerDialog.datePicker.minDate = minDateCalendar.timeInMillis
        datePickerDialog.show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // UI elements (unchanged)
            Text(
                text = "Blood Donor Submission",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.document),
                contentDescription = "Blood Donor Icon",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = !isValidName(name) && name.isNotEmpty()
            )
            if (!isValidName(name) && name.isNotEmpty()) {
                Text(
                    "Name must only contain letters",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // IC (12 digits)
            TextField(
                value = ic,
                onValueChange = { ic = formatIC(it) },
                label = { Text("IC (12 digits number only)") },
                modifier = Modifier.fillMaxWidth(),
                isError = !isValidIC(ic) && ic.isNotEmpty()
            )
            if (!isValidIC(ic) && ic.isNotEmpty()) {
                Text(
                    "IC must be exactly 12 digits (no - or spaces)",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date of Birth (Calendar Picker)
            TextField(
                value = dob,
                onValueChange = { },
                label = { Text("Date of Birth (DD/MM/YYYY)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { openDatePicker() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Age
            TextField(
                value = age,
                onValueChange = { /* Do nothing - read-only */ },
                label = { Text("Age") },
                enabled = false, // Make it read-only
                modifier = Modifier.width(65.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Blood Type (Radio Button)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Blood Type")
                RadioButton(selected = bloodType == "A", onClick = { bloodType = "A" })
                Text("A")
                RadioButton(selected = bloodType == "B", onClick = { bloodType = "B" })
                Text("B")
                RadioButton(selected = bloodType == "O", onClick = { bloodType = "O" })
                Text("O")
                RadioButton(selected = bloodType == "AB", onClick = { bloodType = "AB" })
                Text("AB")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Gender (Radio Button)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Gender")
                RadioButton(selected = gender == "Male", onClick = { gender = "Male" })
                Text("Male")
                RadioButton(selected = gender == "Female", onClick = { gender = "Female" })
                Text("Female")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Phone Number (10-11 digits)
            TextField(
                value = phone,
                onValueChange = { phone = formatPhone(it) },
                label = { Text("No.Phone (10-11 digits number only)") },
                modifier = Modifier.fillMaxWidth(),
                isError = !isValidPhone(phone) && phone.isNotEmpty()
            )
            if (!isValidPhone(phone) && phone.isNotEmpty()) {
                Text(
                    "No.Phone must be 10 or 11 digits number (no - or spaces)",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Email Address
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                isError = !isValidEmail(email) && email.isNotEmpty()
            )
            if (!isValidEmail(email) && email.isNotEmpty()) {
                Text(
                    "Please enter a valid email address",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Address
            TextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Home Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = { handleSubmission() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
            // When the user submits the profile
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

    // AlertDialog for showing messages
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Submission Status") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
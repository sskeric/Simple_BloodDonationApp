# 🩸 Blood Donation App — Donor Profile & Appointment System
A simple Android application built using Jetpack Compose that manages blood donor profiles, generates reference codes, and allows users to book and manage blood donation appointments across multiple hospitals.

# ✨ Features
1. 👤 Donor Profile Management - 
Users can register blood donor profiles with personal details
Includes validation for:
+ Name (letters only)
+ IC number (12 digits)
+ Phone number (10–11 digits)
+ Email format validation
+ Automatically calculates age based on date of birth
+ Generates a unique 6-character reference code for each donor

2. 🔑 Reference Code System
+ Each donor receives a 6-character reference code (A–Z, 1–9)
+ Reference code is linked to IC number
Used to:
+ Access appointment booking
+ Retrieve donor profile
+ Prevent duplicate submissions

3. 🏥 Appointment Booking System - 
Users book appointments using:
+ Reference code
+ Selected hospital
+ Date (limited range: 17–60 years age logic + date restrictions)
+ Time slot selection
+ Prevents duplicate bookings using “USED” flag
+ Stores appointment data locally using SharedPreferences

4. 🏨 Hospital Information System - 
Displays list of blood donation centers in Kedah, Malaysia
Each hospital includes:
+ Image
+ Description
+ Location map image
+ Users can switch between hospitals dynamically

5. 👁 Profile & Appointment Viewer - 
Search donor profile using IC number
+ Displays:
+ Personal details
+ Blood type
+ Contact information
+ Automatically fetches appointment using reference code
Supports:
+ Share profile via other apps (WhatsApp, Gmail, etc.)
+ Delete profile + appointment data

6. 🧭 Navigation System
+ Multi-screen Android app:
+ Welcome Page
+ Main Dashboard
+ Hospital List
+ Profile Submission
+ Appointment Booking
+ Profile Viewer

# 🛠 Tech Stack
+ Frontend: Kotlin (Jetpack Compose UI)
+ Storage: SharedPreferences (local storage only)
+ Android Components: Activities + Intents
+ UI Elements: Compose Material 3
+ Date Handling: Calendar + DatePickerDialog
+ Image Handling: Drawable resources

# 🔄 System Workflow
1. User opens app → lands on welcome page
2. User creates donor profile
3. System validates input and generates reference code
4. Profile is saved locally using IC as identifier
5. User uses reference code to book appointment
6. Appointment is stored separately with reference code
7. User can search profile using IC
System links:
+ IC → Profile
+ Reference Code → Appointment
+ User can view, share, or delete stored data

# 🔒 Data & Security 
All data is stored locally using SharedPreferences
+ No internet server or backend is used
+ No authentication system is implemented
+ No sensitive credentials are stored such as:
+ Passwords
+ API keys
+ Tokens
+ Firebase credentials
+ External database URLs
+ ⚠️ The “reference code” is NOT a security token — it is only a local identifier used to link profile and appointment data.

# ⚙️ Setup Instructions
a. Prerequisites
+ Android Studio (latest recommended)
+ Kotlin support enabled
+ Android SDK installed

b. Clone Project
+ git clone https://github.com/yourusername/blood-donation-app.git
+ cd blood-donation-app

c. Open Project
+ Open Android Studio
+ Select “Open Existing Project”
+ Choose project folder

d. Run Application
+ Connect emulator or physical device
+ Click ▶ Run button

# 🎯 Project Outcome

This project demonstrates:
+ Jetpack Compose UI development
+ Multi-activity Android architecture
+ Local data persistence using SharedPreferences
+ Form validation and input sanitization
+ Date handling and dynamic UI updates
+ Basic identity linking system using reference codes
+ Real-world workflow simulation for healthcare appointment booking

# 📌 Summary
This application is a local Android blood donation management system that allows:
+ Donor registration
+ Profile management
+ Appointment booking
+ Hospital browsing
+ Data viewing, sharing, and deletion

It uses a simple reference-code linking system (not a security token system) and does not involve any backend or external services.

# Video Demostration


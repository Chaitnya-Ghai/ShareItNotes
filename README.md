# 📒 ShareItNotes

A modern, minimalistic **Notes Sharing App** built with **Kotlin**, **Jetpack Compose**, **Firebase**, and **Supabase** — following a **multi-module MVVM architecture**.  
It allows users to **create, edit, delete, read, and share notes**, complete with image uploads stored securely in **Supabase Storage Buckets**.

---

## 🏗️ Tech Overview

| Layer | Technology |
|-------|-------------|
| **UI** | Jetpack Compose + Material 3 |
| **Architecture** | MVVM + Clean Architecture |
| **Backend** | Firebase Firestore + Supabase Storage |
| **Auth** | Firebase Authentication |
| **Async** | Kotlin Coroutines + Flow |
| **DI** | Hilt |
| **Image Loading** | Coil |
| **Build System** | Gradle KTS |

---

## ✨ Features

- 🔐 **Firebase Authentication** (Sign in / Sign up)
- 📝 **Create, Edit, Delete, and Read Notes**
- ☁️ **Cloud Firestore Integration**
- 🧠 **Image Upload via Supabase Buckets**
- 🔄 **Pagination with Lazy Loading**
- ⚙️ **Coroutines + Flow for Reactive UI**
- 🧩 **Multi-Module MVVM Architecture**
- 🎨 **Jetpack Compose with Material 3**
- 💉 **Dependency Injection using Hilt**

---

## 🧱 Project Architecture

The app follows a **multi-module MVVM structure**, ensuring scalability and clear separation of concerns:

ShareItNotes/
│
├── app/ # Application entry module
├── auth/ # Firebase Authentication logic
├── notes/ # Notes CRUD + Firestore + Supabase integration
├── shared-notes/ # Shared notes, Firestore + Supabase integration


---

## 🧭 Project Structure (Visual Overview)

<img width="1917" height="1079" alt="Project Structure" src="https://github.com/user-attachments/assets/66ce9c0a-cf95-4276-80b1-704f3b98ee65" />

---

## 📱 Screenshots

| 🗒️ Notes Screen | 🌐 Shared Notes Grid |
|-----------------|----------------------|
| <img width="514" height="752" alt="Notes Screen" src="https://github.com/user-attachments/assets/a52d8b80-32d6-4e9a-80c8-c2384d0dd5a1" /> | <img width="519" height="768" alt="Shared Notes Grid" src="https://github.com/user-attachments/assets/81bbb878-7fda-4a84-8f48-e6266f508e4f" /> |

| ✏️ Edit Note Screen |
|----------------------|
| <img width="514" height="769" alt="Edit Note Screen" src="https://github.com/user-attachments/assets/35822fa1-8355-4230-8408-d5078a71965a" /> |



---

## 🚀 How to Run

Follow these steps to set up and run **ShareItNotes** locally 👇  

### 1️⃣ Clone the repository
```bash
git clone https://github.com/Chaitnya-Ghai/ShareItNotes.git
cd ShareItNotes

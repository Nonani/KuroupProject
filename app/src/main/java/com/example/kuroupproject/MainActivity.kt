package com.example.kuroupproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kuroupproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.apply {
            registerButton.setOnClickListener {
                val email = id.text.toString()
                val pwd = pwd.text.toString()


                auth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener{ result ->
                        if (result.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            val user = auth.currentUser
                            val db = FirebaseFirestore.getInstance()
                            val userData = hashMapOf(
                                "id" to email,
                                "pwd" to pwd,
                            )
                        // Add a new document with a generated ID
                            db.collection("users").document(user!!.uid)
                                .set(userData)
                                .addOnSuccessListener { documentReference ->
                                    Toast.makeText(this@MainActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Error", "Error adding document", e)
                                }
//                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this@MainActivity, result.exception.toString(), Toast.LENGTH_SHORT).show()
//                            updateUI(null)
                        }
                    }
            }
            loginButton.setOnClickListener {
                val email = id.text.toString()
                val pwd = pwd.text.toString()
                if (email.isNotEmpty() && pwd.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                Log.d("success", "Error adding document")
                                var intent = Intent(this@MainActivity, HomeActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@MainActivity, result.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            }

        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser.reload()
        }
    }
}
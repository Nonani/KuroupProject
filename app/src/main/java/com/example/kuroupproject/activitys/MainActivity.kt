package com.example.kuroupproject.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kuroupproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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

        val intent = intent
        val email: String? = intent.getStringExtra("email")
        val password : String? = intent.getStringExtra("password")

        binding.id.setText(email)
        binding.pwd.setText(password)
    }

    private fun initLayout() {
        binding.apply {
            registerButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, SignUpActivity::class.java))
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
                                Toast.makeText(this@MainActivity, "로그인 정보를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else if(email.isEmpty()){
                    Toast.makeText(this@MainActivity,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@MainActivity,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
                }

            }

        }
        //startActivity(Intent(this@MainActivity,SignUpActivity::class.java))
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
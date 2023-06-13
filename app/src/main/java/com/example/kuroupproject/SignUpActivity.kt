package com.example.kuroupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.kuroupproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initSpinner()
        signup()
    }

    fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
        val pattern = Pattern.compile(emailRegex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun signup() {
        viewBinding.apply {
            registerButton.setOnClickListener {
                val id = idEt.text.toString()
                val pwd = pwdEt.text.toString()
                val name = nameEt.text.toString()
                val phoneNum = phonenumEt.text.toString()
                val gender = if (rgGender.checkedRadioButtonId == R.id.rb_man) "남성" else "여성"
                val location = spinnerLocation.selectedItem.toString()
                val language = spinnerLanguage.selectedItem.toString()
                val mainPart = spinnerMainPart.selectedItem.toString()
                val onelineExplain = onelineExplainEt.text.toString()

                if (!validateEmail(id)) {
                    //빈칸 없는지 체크
                    Toast.makeText(this@SignUpActivity, "유효하지 않은 이메일 형식입니다.", Toast.LENGTH_SHORT)
                        .show()
                    idEt.requestFocus()
                }else if(pwd.isEmpty()){
                    Toast.makeText(this@SignUpActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT)
                        .show()
                    pwdEt.requestFocus()
                }else if(name.isEmpty()){
                    Toast.makeText(this@SignUpActivity, "이름을 입력해주세요.", Toast.LENGTH_SHORT)
                        .show()
                    nameEt.requestFocus()
                } else if(phoneNum.isEmpty()){
                    Toast.makeText(this@SignUpActivity, "연락처를 입력해주세요.", Toast.LENGTH_SHORT)
                        .show()
                    phonenumEt.requestFocus()
                }  else if(onelineExplain.isEmpty()){
                    Toast.makeText(this@SignUpActivity, "한줄 소개를 입력해주세요.", Toast.LENGTH_SHORT)
                        .show()
                    onelineExplainEt.requestFocus()
                } else {
                    //내용 입력이 다 된 경우
                    val scrapDataList = arrayListOf<HashMap<String,String>>()

                    val userData = hashMapOf(
                        "name" to name,
                        "phonenum" to phoneNum,
                        "gender" to gender,
                        "location" to location,
                        "language" to language,
                        "mainPart" to mainPart,
                        "onelineExplain" to onelineExplain,
                        "scrap" to scrapDataList
                    )
                    val auth = FirebaseAuth.getInstance()
                    auth.createUserWithEmailAndPassword(id,pwd)
                        .addOnCompleteListener{task->
                            if(task.isSuccessful){
                                //사용자 등록 성공
                                val user: FirebaseUser?=auth.currentUser
                                val uid:String?=user?.uid

                                val firestore = FirebaseFirestore.getInstance()
                                firestore.collection("users")
                                    .document(uid!!).set(userData)
                                    .addOnSuccessListener { documentReference ->
                                        // 사용자 데이터가 성공적으로 추가되었습니다.
                                        val intent = Intent(this@SignUpActivity,MainActivity::class.java)
                                        intent.putExtra("email",id)
                                        intent.putExtra("password",pwd)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->
                                        // 사용자 데이터 추가 중에 오류가 발생했습니다.
                                    }

                            }
                        }
                }
            }
        }
    }

    private fun initSpinner() {
        val locations = resources.getStringArray(R.array.spinnerLocation)
        val languages = resources.getStringArray(R.array.spinnerLanguage)
        val parts = resources.getStringArray(R.array.spinnerPart)

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        val adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, parts)

        viewBinding.spinnerLocation.adapter=adapter1
        viewBinding.spinnerLanguage.adapter=adapter2
        viewBinding.spinnerMainPart.adapter=adapter3
    }
}
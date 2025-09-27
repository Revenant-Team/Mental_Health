package com.example.metalhealthapp.Screens.AuthPages.SignUp

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metalhealthapp.Model.UserProfile
import com.example.metalhealthapp.Model.UserSignUpReq
import com.example.metalhealthapp.Repo.Repo
import com.example.metalhealthapp.Utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpVM @Inject  constructor(private val repo : Repo): ViewModel() {

    private val _user : MutableStateFlow<UserSignUpReq> = MutableStateFlow(UserSignUpReq("","","",null,null))
    val user : StateFlow<UserSignUpReq> = _user

    private val _profile : MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile())
    val profile : StateFlow<UserProfile> = _profile

    val isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun signUp(context : Context, onSuccess : (String)-> Unit , onFail : (String)-> Unit){
        viewModelScope.launch {
            isLoading.value= true
            updateProfile()
            repo.signUp(user.value)
                .onSuccess {
                    val tokenManager = TokenManager(context)
                    tokenManager.saveToken(it.data.token)
                    onSuccess(it.message)
                    isLoading.value=false
                }
                .onFailure {
                    onFail(it.message?:"Unknown Error")
                    isLoading.value=false
                }
        }
    }

    fun updateEmail(email : String){
        _user.value = _user.value.copy(email = email)
    }
    fun updateUsername(username : String){
        _user.value = _user.value.copy(username = username)
    }
    fun updatePassword(password : String){
        _user.value = _user.value.copy(password = password)
    }
    fun updateInstituteCode(instituteCode : String){
        _user.value = _user.value.copy(instituteCode = instituteCode)
    }
    private fun buildProfileOrNull(): UserProfile? {
        val p = profile.value
        return if (
            p.firstName.isNullOrBlank() &&
            p.lastName.isNullOrBlank() &&
            p.rollNumber.isNullOrBlank() &&
            p.course.isNullOrBlank() &&
            p.year.isNullOrBlank() &&
            p.department.isNullOrBlank()
        ) {
            null
        } else {
            UserProfile(
                firstName = p.firstName?.trim().takeIf { !it.isNullOrBlank() },
                lastName = p.lastName?.trim().takeIf { !it.isNullOrBlank() },
                rollNumber = p.rollNumber?.trim().takeIf { !it.isNullOrBlank() },
                course = p.course?.trim().takeIf { !it.isNullOrBlank() },
                year = p.year?.trim().takeIf { !it.isNullOrBlank() },
                department = p.department?.trim().takeIf { !it.isNullOrBlank() }
            )
        }
    }

    fun updateProfile(){


        _user.value = _user.value.copy(profile = buildProfileOrNull())
    }

    //profile updates
    fun updateFirstName(firstName : String){
        _profile.value = _profile.value.copy(firstName = firstName)
    }
    fun updateLastName(lastName : String){
        _profile.value = _profile.value.copy(lastName = lastName)
    }
    fun updateRollNumber(rollNumber : String){
        _profile.value = _profile.value.copy(rollNumber = rollNumber)
    }
    fun updateCourse(course : String){
        _profile.value = _profile.value.copy(course = course)
    }
    fun updateYear(year : String){
        _profile.value = _profile.value.copy(year = year)
    }
    fun updateDepartment(department : String){
        _profile.value = _profile.value.copy(department = department)
    }


}
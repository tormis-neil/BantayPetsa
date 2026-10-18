package com.bantaypetsa.app.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bantaypetsa.app.core.util.SampleInventoryState
import com.bantaypetsa.app.domain.model.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ProfileUiState(
    val userProfile: UserProfile = UserProfile(),
    val isLoading: Boolean = false
)

class ProfileViewModel : ViewModel() {
    val uiState: StateFlow<ProfileUiState> = SampleInventoryState.userProfile
        .map { profile ->
            ProfileUiState(
                userProfile = profile,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState(isLoading = true)
        )

    fun updateUserProfile(
        storeName: String,
        ownerName: String,
        email: String,
        phone: String,
        businessType: String,
        location: String
    ) {
        SampleInventoryState.updateUserProfile(
            storeName = storeName,
            ownerName = ownerName,
            email = email,
            phone = phone,
            businessType = businessType,
            location = location
        )
    }

    fun toggleArchive() {
        SampleInventoryState.toggleArchiveUserProfile()
    }

    fun resetProfile() {
        SampleInventoryState.resetUserProfile()
    }
}

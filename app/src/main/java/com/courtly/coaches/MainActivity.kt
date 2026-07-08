package com.courtly.coaches

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.courtly.coaches.app.navigation.CoachNavigation
import com.courtly.coaches.contexts.analytics.presentation.viewmodel.AnalyticsViewModel
import com.courtly.coaches.contexts.analytics.presentation.viewmodel.AnalyticsViewModelFactory
import com.courtly.coaches.contexts.coaches.application.usecases.CreateCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.DeleteCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.GetAllCoachesUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.GetMyCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.UpdateCoachUseCase
import com.courtly.coaches.contexts.coaches.infrastructure.remote.CoachApiService
import com.courtly.coaches.contexts.coaches.infrastructure.repository.CoachRepositoryImpl
import com.courtly.coaches.contexts.coaches.presentation.viewmodel.CoachViewModel
import com.courtly.coaches.contexts.coaches.presentation.viewmodel.CoachViewModelFactory
import com.courtly.coaches.contexts.iam.application.usecases.SignInUseCase
import com.courtly.coaches.contexts.iam.application.usecases.SignUpUseCase
import com.courtly.coaches.contexts.iam.infrastructure.remote.AuthenticationApiService
import com.courtly.coaches.contexts.iam.infrastructure.repository.AuthenticationRepositoryImpl
import com.courtly.coaches.contexts.iam.presentation.screens.SignInScreen
import com.courtly.coaches.contexts.iam.presentation.viewmodel.SignInViewModel
import com.courtly.coaches.contexts.iam.presentation.viewmodel.SignInViewModelFactory
import com.courtly.coaches.shared.infrastructure.network.RetrofitClient
import com.courtly.coaches.shared.infrastructure.storage.SessionStorage
import com.courtly.coaches.ui.theme.CourtlyCoachesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val sessionStorage =
            SessionStorage(applicationContext)

        RetrofitClient.initialize(sessionStorage)

        val authenticationApiService =
            RetrofitClient.retrofit.create(
                AuthenticationApiService::class.java
            )

        val authenticationRepository =
            AuthenticationRepositoryImpl(
                apiService = authenticationApiService,
                sessionStorage = sessionStorage
            )

        val signInUseCase =
            SignInUseCase(
                repository = authenticationRepository
            )

        val signUpUseCase =
            SignUpUseCase(
                repository = authenticationRepository
            )

        val coachApiService =
            RetrofitClient.retrofit.create(
                CoachApiService::class.java
            )

        val coachRepository =
            CoachRepositoryImpl(
                apiService = coachApiService
            )

        val analyticsApiService =
            RetrofitClient.retrofit.create(
                com.courtly.coaches.contexts.analytics.infrastructure.remote.AnalyticsApiService::class.java
            )

        val analyticsRepository =
            com.courtly.coaches.contexts.analytics.infrastructure.repository.AnalyticsRepositoryImpl(
                apiService = analyticsApiService
            )

        val analyticsViewModelFactory =
            com.courtly.coaches.contexts.analytics.presentation.viewmodel.AnalyticsViewModelFactory(
                getMyMetricsUseCase =
                    com.courtly.coaches.contexts.analytics.application.usecases.GetMyMetricsUseCase(
                        analyticsRepository
                    )
            )

        val createCoachUseCase =
            CreateCoachUseCase(
                repository = coachRepository
            )

        val signInViewModelFactory =
            SignInViewModelFactory(
                signInUseCase = signInUseCase,
                signUpUseCase = signUpUseCase,
                createCoachUseCase = createCoachUseCase
            )

        val reviewApiService =
            RetrofitClient.retrofit.create(
                com.courtly.coaches.contexts.reviews.infrastructure.remote.ReviewApiService::class.java
            )

        val reviewRepository =
            com.courtly.coaches.contexts.reviews.infrastructure.repository.ReviewRepositoryImpl(
                apiService = reviewApiService
            )

        val getCoachReviewsUseCase =
            com.courtly.coaches.contexts.reviews.application.usecases.GetCoachReviewsUseCase(
                repository = reviewRepository
            )

        val coachViewModelFactory =
            CoachViewModelFactory(
                getMyCoachUseCase =
                    GetMyCoachUseCase(
                        repository = coachRepository
                    ),
                getAllCoachesUseCase =
                    GetAllCoachesUseCase(
                        repository = coachRepository
                    ),
                createCoachUseCase =
                    createCoachUseCase,
                updateCoachUseCase =
                    UpdateCoachUseCase(
                        repository = coachRepository
                    ),
                deleteCoachUseCase =
                    DeleteCoachUseCase(
                        repository = coachRepository
                    ),
                getCoachReviewsUseCase =
                    getCoachReviewsUseCase
            )

        val trainingSessionApiService =
            RetrofitClient.retrofit.create(
                com.courtly.coaches.contexts.trainingsessions.infrastructure.remote.TrainingSessionApiService::class.java
            )

        val trainingSessionRepository =
            com.courtly.coaches.contexts.trainingsessions.infrastructure.repository.TrainingSessionRepositoryImpl(
                apiService = trainingSessionApiService
            )

        val getMyTrainingSessionsUseCase =
            com.courtly.coaches.contexts.trainingsessions.application.usecases.GetMyTrainingSessionsUseCase(
                repository = trainingSessionRepository
            )

        val acceptTrainingSessionUseCase =
            com.courtly.coaches.contexts.trainingsessions.application.usecases.AcceptTrainingSessionUseCase(
                repository = trainingSessionRepository
            )

        val rejectTrainingSessionUseCase =
            com.courtly.coaches.contexts.trainingsessions.application.usecases.RejectTrainingSessionUseCase(
                repository = trainingSessionRepository
            )

        val trainingSessionsViewModelFactory =
            com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel.TrainingSessionsViewModelFactory(
                getMyTrainingSessionsUseCase = getMyTrainingSessionsUseCase,
                acceptTrainingSessionUseCase = acceptTrainingSessionUseCase,
                rejectTrainingSessionUseCase = rejectTrainingSessionUseCase
            )

        setContent {
            CourtlyCoachesTheme {
                CourtlyApp(
                    sessionStorage = sessionStorage,
                    signInViewModelFactory = signInViewModelFactory,
                    coachViewModelFactory = coachViewModelFactory,
                    analyticsViewModelFactory = analyticsViewModelFactory
                    trainingSessionsViewModelFactory = trainingSessionsViewModelFactory
                )
            }
        }
    }
}

@Composable
fun CourtlyApp(
    sessionStorage: SessionStorage,
    signInViewModelFactory: SignInViewModelFactory,
    coachViewModelFactory: CoachViewModelFactory,
    analyticsViewModelFactory: AnalyticsViewModelFactory
    trainingSessionsViewModelFactory: com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel.TrainingSessionsViewModelFactory
) {
    var isAuthenticated by remember {
        mutableStateOf(
            sessionStorage.hasActiveSession()
        )
    }

    if (isAuthenticated) {
        val coachViewModel: CoachViewModel =
            viewModel(
                factory = coachViewModelFactory
            )

        val analyticsViewModel:
                AnalyticsViewModel =
            viewModel(
                factory = analyticsViewModelFactory
              )
            
        val trainingSessionsViewModel: com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel.TrainingSessionsViewModel =
            viewModel(
                factory = trainingSessionsViewModelFactory
            )

        CoachNavigation(
            coachViewModel = coachViewModel,
            analyticsViewModel = analyticsViewModel,
            trainingSessionsViewModel = trainingSessionsViewModel,
            userId = sessionStorage.getUserId()?.toLong() ?: 0L,
            onSignOut = {
                sessionStorage.clearSession()
                isAuthenticated = false
            }
        )
    } else {
        val signInViewModel: SignInViewModel =
            viewModel(
                factory = signInViewModelFactory
            )

        SignInScreen(
            viewModel = signInViewModel,
            onSignInSuccess = {
                isAuthenticated = true
            }
        )
    }
}
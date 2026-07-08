package com.courtly.coaches

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.courtly.coaches.contexts.notifications.application.usecases.GetMyNotificationsUseCase
import com.courtly.coaches.contexts.notifications.application.usecases.GetMyUnreadCountUseCase
import com.courtly.coaches.contexts.notifications.application.usecases.MarkNotificationAsReadUseCase
import com.courtly.coaches.contexts.notifications.infrastructure.remote.NotificationApiService
import com.courtly.coaches.contexts.notifications.infrastructure.repository.NotificationRepositoryImpl
import com.courtly.coaches.contexts.notifications.presentation.viewmodel.NotificationViewModel
import com.courtly.coaches.contexts.notifications.presentation.viewmodel.NotificationViewModelFactory
import com.courtly.coaches.contexts.payments.application.usecases.GetReceivedPaymentsUseCase
import com.courtly.coaches.contexts.payments.infrastructure.remote.PaymentApiService
import com.courtly.coaches.contexts.payments.infrastructure.repository.PaymentRepositoryImpl
import com.courtly.coaches.contexts.payments.presentation.viewmodel.ReceivedPaymentsViewModel
import com.courtly.coaches.contexts.payments.presentation.viewmodel.ReceivedPaymentsViewModelFactory
import com.courtly.coaches.shared.infrastructure.network.RetrofitClient
import com.courtly.coaches.shared.infrastructure.network.SessionEventBus
import com.courtly.coaches.shared.infrastructure.storage.SessionStorage
import com.courtly.coaches.ui.theme.CourtlyCoachesTheme
import com.courtly.coaches.contexts.analytics.infrastructure.remote.AnalyticsApiService
import com.courtly.coaches.contexts.analytics.infrastructure.repository.AnalyticsRepositoryImpl
import com.courtly.coaches.contexts.analytics.application.usecases.GetMyMetricsUseCase

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

        val analyticsApiService = RetrofitClient.retrofit.create(
            AnalyticsApiService::class.java
        )
        val analyticsRepository = AnalyticsRepositoryImpl(
            apiService = analyticsApiService
        )
        val analyticsViewModelFactory = AnalyticsViewModelFactory(
            getMyMetricsUseCase = GetMyMetricsUseCase(analyticsRepository)
        )

        val createCoachUseCase =
            CreateCoachUseCase(
                repository = coachRepository
            )

        val notificationApiService =
            RetrofitClient.retrofit.create(
                NotificationApiService::class.java
            )

        val notificationRepository =
            NotificationRepositoryImpl(
                apiService = notificationApiService
            )

        val paymentApiService =
            RetrofitClient.retrofit.create(
                PaymentApiService::class.java
            )

        val paymentRepository =
            PaymentRepositoryImpl(
                apiService = paymentApiService
            )

        val receivedPaymentsViewModelFactory =
            ReceivedPaymentsViewModelFactory(
                getReceivedPaymentsUseCase = GetReceivedPaymentsUseCase(
                    repository = paymentRepository
                )
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

        val signInViewModelFactory =
            SignInViewModelFactory(
                signInUseCase = signInUseCase,
                signUpUseCase = signUpUseCase,
                createCoachUseCase = createCoachUseCase
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
                    )
            )

        val notificationViewModelFactory =
            NotificationViewModelFactory(
                getMyNotificationsUseCase =
                    GetMyNotificationsUseCase(
                        repository = notificationRepository
                    ),
                markNotificationAsReadUseCase =
                    MarkNotificationAsReadUseCase(
                        repository = notificationRepository
                    ),
                getMyUnreadCountUseCase =
                    GetMyUnreadCountUseCase(
                        repository = notificationRepository
                    )
            )

        setContent {
            CourtlyCoachesTheme {
                CourtlyApp(
                    sessionStorage = sessionStorage,
                    signInViewModelFactory = signInViewModelFactory,
                    coachViewModelFactory = coachViewModelFactory,
                    analyticsViewModelFactory = analyticsViewModelFactory,
                    notificationViewModelFactory = notificationViewModelFactory,
                    receivedPaymentsViewModelFactory = receivedPaymentsViewModelFactory,
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
    analyticsViewModelFactory: AnalyticsViewModelFactory,
    notificationViewModelFactory: NotificationViewModelFactory,
    receivedPaymentsViewModelFactory: ReceivedPaymentsViewModelFactory,
    trainingSessionsViewModelFactory: com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel.TrainingSessionsViewModelFactory
) {
    var isAuthenticated by remember {
        mutableStateOf(
            sessionStorage.hasActiveSession()
        )
    }

    LaunchedEffect(Unit) {
        SessionEventBus.sessionExpiredEvents.collect {
            isAuthenticated = false
        }
    }

    if (isAuthenticated) {
        val coachViewModel: CoachViewModel =
            viewModel(factory = coachViewModelFactory)
        val analyticsViewModel: AnalyticsViewModel =
            viewModel(factory = analyticsViewModelFactory)
        val notificationViewModel: NotificationViewModel =
            viewModel(factory = notificationViewModelFactory)
        val trainingSessionsViewModel: com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel.TrainingSessionsViewModel =
            viewModel(factory = trainingSessionsViewModelFactory)
        val receivedPaymentsViewModel: ReceivedPaymentsViewModel =
            viewModel(factory = receivedPaymentsViewModelFactory)
        CoachNavigation(
            coachViewModel = coachViewModel,
            analyticsViewModel = analyticsViewModel,
            notificationViewModel = notificationViewModel,
            trainingSessionsViewModel = trainingSessionsViewModel,
            receivedPaymentsViewModel = receivedPaymentsViewModel,
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

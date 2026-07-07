package com.courtly.coaches.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.courtly.coaches.app.navigation.components.AVAILABILITY_ROUTE
import com.courtly.coaches.app.navigation.components.CourtlyCoachBottomBar
import com.courtly.coaches.app.navigation.components.HOME_ROUTE
import com.courtly.coaches.app.navigation.components.MATCHES_ROUTE
import com.courtly.coaches.app.navigation.components.PROFILE_ROUTE
import com.courtly.coaches.app.navigation.components.SESSIONS_ROUTE
import com.courtly.coaches.contexts.coaches.presentation.screens.CoachProfileScreen
import com.courtly.coaches.contexts.coaches.presentation.screens.CreateCoachScreen
import com.courtly.coaches.contexts.coaches.presentation.screens.EditCoachScreen
import com.courtly.coaches.contexts.coaches.presentation.viewmodel.CoachViewModel
import com.courtly.coaches.ui.theme.Background
import com.courtly.coaches.ui.theme.DarkNavy
import com.courtly.coaches.ui.theme.Primary
import com.courtly.coaches.ui.theme.TextSecondary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import com.courtly.coaches.contexts.availabilities.presentation.screens.CoachAvailabilityScreen

private const val CREATE_COACH_ROUTE = "create_coach"
private const val EDIT_COACH_ROUTE = "edit_coach"

@Composable
fun CoachNavigation(
    coachViewModel: CoachViewModel,
    trainingSessionsViewModel: com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel.TrainingSessionsViewModel,
    userId: Long,
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    val coachUiState by coachViewModel.uiState.collectAsState()

    val currentBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        currentBackStackEntry?.destination?.route

    val mainRoutes = setOf(
        HOME_ROUTE,
        AVAILABILITY_ROUTE,
        SESSIONS_ROUTE,
        MATCHES_ROUTE,
        PROFILE_ROUTE
    )

    val showBottomBar =
        currentRoute in mainRoutes

    LaunchedEffect(currentRoute, coachUiState.coach?.id) {
        if (currentRoute in setOf(AVAILABILITY_ROUTE, PROFILE_ROUTE) &&
            coachUiState.coach == null
        ) {
            coachViewModel.loadMyCoach()
        }
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (showBottomBar) {
                CourtlyCoachBottomBar(
                    selectedRoute = currentRoute,
                    onNavigateToHome = {
                        navigateToMainTab(
                            navController = navController,
                            route = HOME_ROUTE
                        )
                    },
                    onNavigateToAvailability = {
                        navigateToMainTab(
                            navController = navController,
                            route = AVAILABILITY_ROUTE
                        )
                    },
                    onNavigateToSessions = {
                        navigateToMainTab(
                            navController = navController,
                            route = SESSIONS_ROUTE
                        )
                    },
                    onNavigateToMatches = {
                        navigateToMainTab(
                            navController = navController,
                            route = MATCHES_ROUTE
                        )
                    },
                    onNavigateToProfile = {
                        navigateToMainTab(
                            navController = navController,
                            route = PROFILE_ROUTE
                        )
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(HOME_ROUTE) {
                CoachHomeScreen(
                    onOpenProfile = {
                        navigateToMainTab(
                            navController = navController,
                            route = PROFILE_ROUTE
                        )
                    }
                )
            }

            composable(AVAILABILITY_ROUTE) {
                val coachId = coachUiState.coach?.id

                if (coachId != null) {
                    CoachAvailabilityScreen(
                        coachId = coachId
                    )
                } else {
                    CoachAvailabilityLoadingScreen()
                }
            }

            composable(SESSIONS_ROUTE) {
                com.courtly.coaches.contexts.trainingsessions.presentation.screens.TrainingSessionsScreen(
                    viewModel = trainingSessionsViewModel
                )
            }

            composable(MATCHES_ROUTE) {
                CoachMatchesPlaceholderScreen()
            }

            composable(PROFILE_ROUTE) {
                CoachProfileScreen(
                    viewModel = coachViewModel,
                    onCreateProfile = {
                        navController.navigate(
                            CREATE_COACH_ROUTE
                        )
                    },
                    onEditProfile = {
                        navController.navigate(
                            EDIT_COACH_ROUTE
                        )
                    },
                    onSignOut = onSignOut
                )
            }

            composable(CREATE_COACH_ROUTE) {
                CreateCoachScreen(
                    viewModel = coachViewModel,
                    userId = userId,
                    onProfileCreated = {
                        navController.navigate(
                            PROFILE_ROUTE
                        ) {
                            popUpTo(
                                CREATE_COACH_ROUTE
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(EDIT_COACH_ROUTE) {
                val uiState by coachViewModel.uiState.collectAsState()
                val coach = uiState.coach

                if (coach != null) {
                    EditCoachScreen(
                        coach = coach,
                        viewModel = coachViewModel,
                        onProfileUpdated = {
                            navController.navigate(PROFILE_ROUTE) {
                                popUpTo(EDIT_COACH_ROUTE) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        coachViewModel.loadMyCoach()
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Primary
                        )
                    }
                }
            }
        }
    }
}

private fun navigateToMainTab(
    navController: NavHostController,
    route: String
) {
    if (
        navController.currentDestination?.route ==
        route
    ) {
        return
    }

    navController.navigate(route) {
        popUpTo(
            navController.graph
                .findStartDestination()
                .id
        ) {
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun CoachHomeScreen(
    onOpenProfile: () -> Unit
) {
    SimpleCoachSectionScreen(
        title = "Inicio",
        description =
            "Bienvenido a tu panel profesional de Courtly.",
        icon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = Primary
            )
        }
    )
}

@Composable
private fun CoachAvailabilityPlaceholderScreen() {
    SimpleCoachSectionScreen(
        title = "Horarios",
        description =
            "Aquí podrás registrar y administrar tu disponibilidad.",
        icon = {
            Icon(
                imageVector =
                    Icons.Default.DateRange,
                contentDescription = null,
                tint = Primary
            )
        }
    )
}

@Composable
private fun CoachSessionsPlaceholderScreen() {
    SimpleCoachSectionScreen(
        title = "Sesiones",
        description =
            "Aquí se mostrarán tus sesiones de entrenamiento.",
        icon = {
            Icon(
                imageVector =
                    Icons.Default.Groups,
                contentDescription = null,
                tint = DarkNavy
            )
        }
    )
}

@Composable
private fun CoachMatchesPlaceholderScreen() {
    SimpleCoachSectionScreen(
        title = "Partidos",
        description =
            "Aquí podrás consultar tus próximos partidos y actividades.",
        icon = {
            Icon(
                imageVector =
                    Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = Primary
            )
        }
    )
}

@Composable
private fun SimpleCoachSectionScreen(
    title: String,
    description: String,
    icon: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {
            icon()

            Text(
                text = title,
                color = DarkNavy,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = description,
                color = TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    horizontal = 32.dp,
                    vertical = 10.dp
                )
            )
        }
    }
}

@Composable
private fun CoachAvailabilityLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Primary
        )
    }
}

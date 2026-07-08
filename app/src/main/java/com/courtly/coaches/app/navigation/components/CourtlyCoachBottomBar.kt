package com.courtly.coaches.app.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.courtly.coaches.ui.theme.DarkNavy
import com.courtly.coaches.ui.theme.Primary
import androidx.compose.material.icons.filled.BarChart

@Composable
fun CourtlyCoachBottomBar(
    selectedRoute: String?,
    onNavigateToHome: () -> Unit,
    onNavigateToAvailability: () -> Unit,
    onNavigateToSessions: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAnalytics: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .navigationBarsPadding()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .align(Alignment.BottomCenter),
            color = DarkNavy,
            shape = RoundedCornerShape(
                topStart = 22.dp,
                topEnd = 22.dp
            ),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 12.dp,
                        bottom = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CourtlyBottomBarItem(
                    icon = Icons.Default.Home,
                    label = "Inicio",
                    selected = selectedRoute == HOME_ROUTE,
                    onClick = onNavigateToHome
                )

                CourtlyBottomBarItem(
                    icon = Icons.Default.DateRange,
                    label = "Horarios",
                    selected = selectedRoute == AVAILABILITY_ROUTE,
                    onClick = onNavigateToAvailability
                )

                Spacer(
                    modifier = Modifier.width(72.dp)
                )

                CourtlyBottomBarItem(
                    icon = Icons.Default.BarChart,
                    label = "Stats",
                    selected = selectedRoute == ANALYTICS_ROUTE,
                    onClick = onNavigateToAnalytics
                )

                CourtlyBottomBarItem(
                    icon = Icons.Default.Person,
                    label = "Perfil",
                    selected = selectedRoute == PROFILE_ROUTE,
                    onClick = onNavigateToProfile
                )
            }
        }

        CourtlyCenterButton(
            selected = selectedRoute == SESSIONS_ROUTE,
            onClick = onNavigateToSessions,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 2.dp)
        )
    }
}

@Composable
private fun CourtlyBottomBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val itemColor = if (selected) {
        Primary
    } else {
        Color(0xFF94A3B8)
    }

    Column(
        modifier = Modifier
            .width(64.dp)
            .height(62.dp)
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(
                horizontal = 2.dp,
                vertical = 5.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = itemColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = label,
            color = itemColor,
            fontSize = 11.sp,
            lineHeight = 12.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            },
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun CourtlyCenterButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .minimumInteractiveComponentSize()
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(Primary)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = "Sesiones",
                tint = DarkNavy,
                modifier = Modifier.size(27.dp)
            )
        }
    }
}

const val HOME_ROUTE = "home"
const val AVAILABILITY_ROUTE = "availability"
const val SESSIONS_ROUTE = "sessions"
const val MATCHES_ROUTE = "matches"
const val ANALYTICS_ROUTE = "analytics"
const val PROFILE_ROUTE = "profile"

package com.courtly.coaches.contexts.analytics.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.courtly.coaches.contexts.analytics.domain.model.Metric

@Composable
fun MetricCard(
    metric: Metric
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = metric.metricType.displayName,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = metric.value.toString(),
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = metric.period,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
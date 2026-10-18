package com.bantaypetsa.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bantaypetsa.app.core.designsystem.theme.AmberApproaching
import com.bantaypetsa.app.core.designsystem.theme.AmberApproachingContainer
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRedContainer
import com.bantaypetsa.app.core.designsystem.theme.OrangeAlert
import com.bantaypetsa.app.core.designsystem.theme.OrangeAlertContainer
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreen
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreenContainer
import com.bantaypetsa.app.domain.model.ExpiryStatus

@Composable
fun ExpiryStatusBadge(
    status: ExpiryStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, icon, label) = when (status) {
        ExpiryStatus.EXPIRED -> Tuple4(
            ExpenseRedContainer,
            ExpenseRed,
            Icons.Default.Dangerous,
            "Expired"
        )
        ExpiryStatus.URGENT -> Tuple4(
            OrangeAlertContainer,
            OrangeAlert,
            Icons.Default.Warning,
            "Urgent"
        )
        ExpiryStatus.APPROACHING -> Tuple4(
            AmberApproachingContainer,
            AmberApproaching,
            Icons.Default.HourglassTop,
            "Approaching"
        )
        ExpiryStatus.SAFE -> Tuple4(
            SuccessGreenContainer,
            SuccessGreen,
            Icons.Default.CheckCircle,
            "Safe"
        )
    }

    Row(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

private data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

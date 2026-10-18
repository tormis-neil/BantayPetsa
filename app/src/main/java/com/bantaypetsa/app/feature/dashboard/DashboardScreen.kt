package com.bantaypetsa.app.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bantaypetsa.app.core.designsystem.component.ExpiryStatusBadge
import com.bantaypetsa.app.core.designsystem.theme.AmberApproaching
import com.bantaypetsa.app.core.designsystem.theme.AmberApproachingContainer
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRed
import com.bantaypetsa.app.core.designsystem.theme.ExpenseRedContainer
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.OrangeAlert
import com.bantaypetsa.app.core.designsystem.theme.OrangeAlertContainer
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreen
import com.bantaypetsa.app.core.designsystem.theme.SuccessGreenContainer
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import com.bantaypetsa.app.core.util.SampleInventoryState
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToAlerts: (String) -> Unit,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header: Welcome back + Settings Icon Button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Welcome back",
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                        color = CharcoalGreen
                    )
                    Text(
                        text = "Retail Inventory Overview",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CharcoalGreen.copy(alpha = 0.7f)
                    )
                }

                IconButton(
                    onClick = onNavigateToSettings,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(PaleGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Section Title: Inventory Status Cards
        item {
            Text(
                text = "Inventory Status",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalGreen
            )
        }

        // 2x2 Grid of 4-Tier Status Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TarsiMetricCard(
                        title = "Expired",
                        count = uiState.expiredCount.toString(),
                        subtitle = "Needs removal",
                        icon = Icons.Default.Dangerous,
                        accentColor = ExpenseRed,
                        containerColor = ExpenseRedContainer,
                        percentage = uiState.expiredPct,
                        onClick = { onNavigateToAlerts("Expired") },
                        modifier = Modifier.weight(1f)
                    )
                    TarsiMetricCard(
                        title = "Urgent",
                        count = uiState.urgentCount.toString(),
                        subtitle = "0 - 7 days left",
                        icon = Icons.Default.Warning,
                        accentColor = OrangeAlert,
                        containerColor = OrangeAlertContainer,
                        percentage = uiState.urgentPct,
                        onClick = { onNavigateToAlerts("Urgent") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TarsiMetricCard(
                        title = "Approaching",
                        count = uiState.approachingCount.toString(),
                        subtitle = "8 - 30 days left",
                        icon = Icons.Default.HourglassTop,
                        accentColor = AmberApproaching,
                        containerColor = AmberApproachingContainer,
                        percentage = uiState.approachingPct,
                        onClick = { onNavigateToAlerts("Approaching") },
                        modifier = Modifier.weight(1f)
                    )
                    TarsiMetricCard(
                        title = "Safe Stock",
                        count = uiState.safeCount.toString(),
                        subtitle = "Fresh batches",
                        icon = Icons.Default.CheckCircle,
                        accentColor = SuccessGreen,
                        containerColor = SuccessGreenContainer,
                        percentage = uiState.safePct,
                        onClick = { onNavigateToAlerts("Safe") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Catalog Summary Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                    .clickable { onNavigateToProducts() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PaleGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Active Store Catalog",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalGreen
                            )
                            Text(
                                text = "${uiState.activeProductCount} Products  \u2022  ${uiState.totalBatchesCount} Batches Monitored",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalGreen.copy(alpha = 0.7f)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "View products",
                        tint = MutedGreen
                    )
                }
            }
        }

        // Quick Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onNavigateToAddProduct,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "New Product",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                OutlinedButton(
                    onClick = { onNavigateToAlerts("All") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "View Alerts",
                        fontWeight = FontWeight.SemiBold,
                        color = CharcoalGreen,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Priority Items Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Priority FEFO Attention",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalGreen
                    )
                    Text(
                        text = "First Expired, First Out stock prioritizing",
                        style = MaterialTheme.typography.bodySmall,
                        color = CharcoalGreen.copy(alpha = 0.7f)
                    )
                }
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    modifier = Modifier
                        .clickable { onNavigateToAlerts("All") }
                        .padding(4.dp)
                )
            }
        }

        if (uiState.attentionBatches.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = "All Batches Are Safe!",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalGreen
                            )
                            Text(
                                text = "No expired or urgent items currently detected.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalGreen.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        } else {
            items(uiState.attentionBatches, key = { it.batchId }) { batch ->
                val prod = uiState.products.find { it.productId == batch.productId }
                val status = SampleInventoryState.calculateStatus(batch.expirationDate)
                val formatter = remember { DateTimeFormatter.ofPattern("MMM dd, yyyy") }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .clickable { onNavigateToProductDetail(batch.productId) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Title and Expiry Column with guaranteed 12dp end padding to prevent badge overlap
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = prod?.name ?: "Unknown Product",
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalGreen,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Expires: ${batch.expirationDate.format(formatter)}  |  ${if (batch.quantity % 1.0 == 0.0) batch.quantity.toInt() else batch.quantity} ${prod?.unit ?: "units"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalGreen.copy(alpha = 0.7f)
                            )
                        }

                        // Badge and Arrow with fixed snug alignment
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ExpiryStatusBadge(status = status)
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MutedGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarsiMetricCard(
    title: String,
    count: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    containerColor: Color,
    percentage: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(containerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    modifier = Modifier
                        .background(containerColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Text(
                text = count,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = CharcoalGreen
            )

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalGreen
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalGreen.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

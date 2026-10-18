package com.bantaypetsa.app.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bantaypetsa.app.R
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation properties
    val logoScale = remember { Animatable(0.65f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(30f) }
    val badgeAlpha = remember { Animatable(0f) }
    val glowScale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        // Stage 1: Logo spring bounce & fade in
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            )
        }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            glowScale.animateTo(
                targetValue = 1.15f,
                animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
            )
        }

        // Stage 2: Slide and fade brand text
        delay(250)
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
            )
        }
        launch {
            textOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }

        // Stage 3: Badge and Tagline fade in
        delay(150)
        badgeAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        )

        // Hold presentation briefly for comfortable brand reading (1.4s total)
        delay(700)
        onSplashComplete()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SoftWhite,
                        PaleGreen.copy(alpha = 0.25f),
                        SoftWhite
                    )
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // Allow instant skip on tap
                onSplashComplete()
            },
        contentAlignment = Alignment.Center
    ) {
        // Subtle ambient radial glow behind logo
        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(glowScale.value)
                .alpha(logoAlpha.value * 0.4f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PaleGreen,
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // Animated Logo Card
            Surface(
                modifier = Modifier
                    .size(130.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(28.dp),
                        spotColor = PrimaryGreen.copy(alpha = 0.3f),
                        ambientColor = PrimaryGreen.copy(alpha = 0.15f)
                    ),
                shape = RoundedCornerShape(28.dp),
                color = SurfaceWhite
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "BantayPetsa Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(22.dp))
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Animated Brand Typography & Slogan
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset { IntOffset(0, textOffsetY.value.toInt()) }
                    .alpha(textAlpha.value)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bantay",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CharcoalGreen,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Petsa",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen,
                        letterSpacing = (-0.5).sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Bantay sa petsa. Alaga sa paninda.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryGreen,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Pill Badge: Sari-Sari Retail Assistant
            Surface(
                shape = RoundedCornerShape(50),
                color = PaleGreen,
                modifier = Modifier.alpha(badgeAlpha.value)
            ) {
                Text(
                    text = "SARI-SARI STORE INVENTORY & EXPIRY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }

        // Footer Brand Attribution
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
                .alpha(badgeAlpha.value * 0.85f)
        ) {
            Text(
                text = "Offline-First Assistant",
                style = MaterialTheme.typography.labelSmall,
                color = MutedGreen,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "v1.0 • Designed for Filipino Retail",
                style = MaterialTheme.typography.labelSmall,
                color = MutedGreen.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
    }
}

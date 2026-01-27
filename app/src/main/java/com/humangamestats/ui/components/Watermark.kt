package com.humangamestats.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.humangamestats.R

/**
 * A watermark logo component that displays in the lower portion of screens.
 * Designed to be used as a background layer beneath scrollable content.
 *
 * @param modifier Optional modifier for customization
 * @param size Size of the watermark logo (default 240.dp)
 * @param alpha Opacity of the watermark (default 1.0f - uses image's built-in transparency)
 * @param bottomPadding Padding from the bottom of the container (default 120.dp to position higher)
 */
@Composable
fun Watermark(
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    alpha: Float = 0.75f,
    bottomPadding: Dp = 100.dp
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_watermark),
            contentDescription = null, // Decorative, no accessibility needed
            modifier = Modifier
                .padding(bottom = bottomPadding)
                .size(size)
                .alpha(alpha),
            contentScale = ContentScale.Fit
        )
    }
}

/**
 * A container that wraps content with a watermark in the background.
 * The watermark appears in the lower portion of the container, beneath the content.
 *
 * @param modifier Modifier for the container
 * @param watermarkSize Size of the watermark logo
 * @param watermarkAlpha Opacity of the watermark
 * @param content The main content to display above the watermark
 */
@Composable
fun WatermarkContainer(
    modifier: Modifier = Modifier,
    watermarkSize: Dp = 240.dp,
    watermarkAlpha: Float = 1.0f,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Watermark as background layer
        Watermark(
            size = watermarkSize,
            alpha = watermarkAlpha
        )
        
        // Main content on top
        content()
    }
}

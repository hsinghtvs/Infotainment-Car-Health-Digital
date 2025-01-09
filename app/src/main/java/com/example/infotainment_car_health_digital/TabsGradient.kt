package com.example.infotainment_car_health_digital

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val HALF_INDICATOR_WIDTH = 5.dp
private val DEFAULT_INDICATOR_POSITION = 0.dp
private const val ANIM_DURATION = 250

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun Tabs(
    tabs: List<TabItem>,
    onTabSelect: (TabItem) -> Unit,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
) {
    val buttonBackGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF255AF5).copy(alpha = 1f),
            Color(0xFF0B1112).copy(alpha = 0.1f)
        )
    )

    val itemsBorderGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFFFFFFF).copy(alpha = 0f),
            Color(0xFFFFFFFF).copy(alpha = 0.1f),
            Color(0xFF032B9D).copy(alpha = 0.4f)
        )
    )

    val rowBackGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF2EA7FF).copy(alpha = 0f),
            Color(0xFF2EA7FF).copy(alpha = 0f),
            Color(0xFF2EA7FF).copy(alpha = 0f),
            Color(0xFF2EA7FF).copy(alpha = 0.1f)
        )
    )


    val transparentGradient = Brush.verticalGradient(
        listOf(
            Color.Transparent,
            Color.Transparent
        )
    )

    val density = LocalDensity.current
    val tabIndicatorPositions = remember { mutableStateListOf<Dp>() }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            LazyRow(
                modifier = Modifier
                    .background(
                        brush = rowBackGroundGradient,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .border(1.dp, brush = itemsBorderGradient, shape = RoundedCornerShape(30.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    tabs.forEachIndexed { index, tabItem ->

                        Text(
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    if (tabIndicatorPositions.size < tabs.size) {
                                        val position =
                                            with(density) { coordinates.boundsInRoot().bottomCenter.x.toDp() } -
                                                    HALF_INDICATOR_WIDTH
                                        if (position >= DEFAULT_INDICATOR_POSITION) {
                                            tabIndicatorPositions.add(position)
                                        }
                                    }
                                }
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
                                    onTabSelect(tabItem)
                                }
                                .background(
                                    brush = if (selectedIndex == index) buttonBackGroundGradient else transparentGradient,
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .padding(10.dp),
                            text = tabItem.title,
                            maxLines = 1,
                            style = TextStyle(
                                color = Color.White, fontSize = 14.sp, fontFamily = FontFamily(
                                    Font(R.font.manrope_bold)
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

data class TabItem(
    val id: Int,
    val title: String,
)

@Preview
@Composable
fun PreviewTabs() {
    val tabs = listOf(
        TabItem(0, "Service Estimate"),
        TabItem(1, "Inspection Report"),
        TabItem(2, "Inspection Pictures")
    )
    var currentTabSelect by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
    ) {
        Tabs(
            tabs = tabs,
            onTabSelect = {
                currentTabSelect = it.id
            },
            selectedIndex = currentTabSelect,
        )
    }
}

package com.android.cardstacklayout

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class CardItem(
    val name: String,
    val surname: String,
    val number: String,
    val month: String,
    val year: String,
    val cvv: String,
    val color: Color
)


private const val finalPoint = 600

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardStackLayout(modifier: Modifier = Modifier, list: List<CardItem>) {
    var frontIndex by remember { mutableStateOf(0) }
    val loopCount = if (list.size < 3) list.size else 3
    var update by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        frontIndex = if (loopCount == 3) 2
        else list.size - 1
    }

    AnimatedContent(targetState = update, transitionSpec = {
        fadeIn() with fadeOut()
    }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            for (i in 0 until loopCount) cardItemCompose(
                frontIndex = frontIndex,
                index = i,
                list = list,
                updateFrontIndex = {
                    frontIndex = it
                    update = !update
                }
            )
        }

    }
}

fun getAlpha(index: Int, dragState: Float): Float {
    val alpha = (finalPoint - dragState) / finalPoint
    return if (index == 2) (if (alpha < 0) 0f else if (alpha > 1) 1f else alpha) else 1f
}

fun nextIndex(currentIndex: Int, size: Int): Int {
    return if (currentIndex == size - 1) 0
    else currentIndex + 1
}

fun prevIndex(currentIndex: Int, size: Int): Int {
    return if (currentIndex == 0) size - 1
    else currentIndex - 1
}


@Composable
fun cardItemCompose(
    frontIndex: Int,
    index: Int,
    list: List<CardItem>,
    updateFrontIndex: (Int) -> Unit
) {
    val firstPadding: Dp by animateDpAsState(targetValue = 0.dp)
    val secondPadding: Dp by animateDpAsState(targetValue = 4.dp)
    val lastPadding: Dp by animateDpAsState(targetValue = 8.dp)
    var dragState by remember { mutableStateOf(0f) }
    val dragAnimation: Float by animateFloatAsState(targetValue = dragState)
    val scope = rememberCoroutineScope()


    fun getPadding(index: Int): Dp {
        return when (index) {
            0 -> firstPadding
            1 -> secondPadding
            else -> lastPadding
        }
    }

    val item = when (index) {
        0 -> {
            list[prevIndex(prevIndex(frontIndex, list.size), list.size)]
        }

        1 -> {
            list[prevIndex(frontIndex, list.size)]
        }

        else -> {
            list[frontIndex]
        }
    }

    return Box(
        Modifier
            .padding(vertical = getPadding(index))
            .padding(end = getPadding(index))
            .fillMaxWidth()
            .height(100.dp)
            .graphicsLayer(translationY = if (index == 2) dragAnimation else 0f)
            .background(item.color.copy(alpha = getAlpha(index, dragState)))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, offset ->
                        change.consumeAllChanges()
                        dragState += offset.y
                        Log.e("dragstate", "$dragState")
                    },
                    onDragEnd = {
                        scope.launch {
                            delay(100)
                            if (dragState < -200) {
                                updateFrontIndex.invoke(nextIndex(frontIndex, list.size))
                            } else if (dragState >= finalPoint) {
                                updateFrontIndex.invoke(prevIndex(frontIndex, list.size))
                            } else {
                                dragState = 0f
                            }
                        }
                    }
                )
            },
    ) {
        Text(text = item.name, style = TextStyle(color = Color.White, fontSize = 30.sp))
    }
}


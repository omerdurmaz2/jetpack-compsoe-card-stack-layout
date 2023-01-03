package com.android.cardstacklayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.android.cardstacklayout.ui.theme.CardStackLayoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardStackLayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val cards = listOf(
                        CardItem(
                            name = "Omer",
                            surname = "Durmaz",
                            number = "3123123154673456",
                            month = "23",
                            year = "2024",
                            cvv = "345",
                            color = Color.Red
                        ),
                        CardItem(
                            name = "Furkan",
                            surname = "Kale",
                            number = "3123123154673456",
                            month = "23",
                            year = "2024",
                            cvv = "345",
                            color = Color.Black
                        ),
                        CardItem(
                            name = "Ensar",
                            surname = "Ungoren",
                            number = "3123123154673456",
                            month = "23",
                            year = "2024",
                            cvv = "345",
                            color = Color.Blue

                        ),
                        CardItem(
                            name = "Mehmet",
                            surname = "Ozmen",
                            number = "3123123154673456",
                            month = "23",
                            year = "2024",
                            cvv = "345",
                            color = Color.Cyan
                        )
                    )
                    CardStackLayout(modifier = Modifier.fillMaxSize(), list = cards)
                }
            }
        }
    }
}

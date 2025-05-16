@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.*

@Suppress("ktlint:standard:function-naming")
@Composable
fun ColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit,
) {
    val controller = rememberColorPickerController()
    val selectedColor by remember { derivedStateOf { controller.selectedColor.value } }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.extraLarge) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
            ) {
                Text(
                    "Выберите цвет",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(selectedColor)
                            .border(1.dp, Color.Gray)
                    )
                }

                Spacer(Modifier.height(16.dp))

                HsvColorPicker(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    controller = controller,
                    initialColor = initialColor,
                    onColorChanged = {},
                )

                Spacer(Modifier.height(8.dp))
                AlphaSlider(controller = controller, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onColorSelected(selectedColor)
                            onDismiss()
                        },
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
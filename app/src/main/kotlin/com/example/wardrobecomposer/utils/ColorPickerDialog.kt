@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Suppress("ktlint:standard:function-naming")
@Composable
fun ColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit,
) {
    val controller = rememberColorPickerController()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.width(300.dp),
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Выберите цвет", style = MaterialTheme.typography.titleLarge)
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
                            onColorSelected(controller.selectedColor.value)
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

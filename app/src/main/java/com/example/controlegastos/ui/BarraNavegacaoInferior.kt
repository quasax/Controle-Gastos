package com.example.controlegastos.ui.components // Ajuste o pacote conforme sua estrutura

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BarraNavegacaoInferior(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    onAdicionarDespesa: () -> Unit
) {
    val activeColor = Color(0xFF1B6B4A)
    val inactiveColor = Color(0xFF9BA1A6)
    val barHeight = 76.dp
    val fabSize = 60.dp
    val fabProtrusion = 22.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight + fabProtrusion),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Home,
                    label = "Início",
                    isSelected = (selectedIndex == 0),
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                ) { onItemSelected(0) }

                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Assignment,
                    label = "Transações",
                    isSelected = (selectedIndex == 1),
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                ) { onItemSelected(1) }

                // Espaço central reservado para o botão "Novo"
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "Novo",
                        color = inactiveColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.BarChart,
                    label = "Gastos",
                    isSelected = (selectedIndex == 2),
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                ) { onItemSelected(2) }

                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Settings,
                    label = "Edição",
                    isSelected = (selectedIndex == 3),
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                ) { onItemSelected(3) }
            }
        }

        FloatingActionButton(
            onClick = onAdicionarDespesa,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(fabSize),
            containerColor = activeColor,
            contentColor = Color.White,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 10.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adicionar despesa",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    onClick: () -> Unit
) {
    val color = if (isSelected) activeColor else inactiveColor

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(26.dp)
        )

        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}
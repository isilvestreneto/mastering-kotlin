package proway.capgemini.kotlin.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Cottage
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector

enum class Categoria(val icon: ImageVector) {
    CASA(Icons.Default.Cottage),
    LAZER(Icons.Default.SportsEsports),
    SAUDE(Icons.Default.HealthAndSafety),
    OUTROS(Icons.Default.Category)
}
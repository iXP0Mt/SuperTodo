package com.ixp0mt.supertodo.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ixp0mt.supertodo.R
import com.ixp0mt.supertodo.domain.model.FolderInfo

@Composable
fun ST_FolderCard(
    item: FolderInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clickable(
                onClick = { onClick() }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
            ,verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_folder),
                contentDescription = null
            )

            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription = null
            )
        }
    }
}
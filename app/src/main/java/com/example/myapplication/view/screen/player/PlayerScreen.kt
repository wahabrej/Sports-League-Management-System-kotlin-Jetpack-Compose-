import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.PlayerTab
import com.example.myapplication.viewmodel.PlayerViewModel
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        //  Top Tabs
        TabRow(
            selectedTabIndex = uiState.selectedTab.ordinal
        ) {

            PlayerTab.entries.forEach { tab ->

                Tab(
                    selected = uiState.selectedTab == tab,
                    onClick = { viewModel.selectTab(tab) },
                    text = { Text(tab.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        //  Toggle Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Active Only")

            Switch(
                checked = uiState.isToggleOn,
                onCheckedChange = { viewModel.toggleChange(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //  Player List
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            items(uiState.players) { player ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = player,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

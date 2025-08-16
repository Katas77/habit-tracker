package com.example.habit_tracker.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit_tracker.viewmodel.ReminderViewModel
import com.example.habit_tracker.data.model.Reminder

@Composable
fun reminderApp(vm: ReminderViewModel) {
    val reminders by vm.reminders.collectAsState()

    var title by remember { mutableStateOf("") }
    var period by remember { mutableStateOf(15L) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("RemindMe") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            PeriodPicker(selected = period, onSelected = { period = it })

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    vm.addReminder(title, period)
                    title = ""
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить напоминание")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Список напоминаний", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(reminders) { r ->
                    ReminderItem(
                        reminder = r,
                        onDelete = { vm.deleteReminder(r) },
                        onToggle = { vm.toggleEnabled(r) }
                    )
                }
            }
        }
    }
}

@Composable
fun PeriodPicker(selected: Long, onSelected: (Long) -> Unit) {
    val options = listOf(
        "15 минут" to 15L,
        "30 минут" to 30L,
        "1 час" to 60L,
        "24 часа" to 24 * 60L
    )
    Column {
        Text("Периодичность", style = MaterialTheme.typography.subtitle1)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { (label, minutes) ->
                val selectedState = selected == minutes
                OutlinedButton(
                    onClick = { onSelected(minutes) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = if (selectedState) MaterialTheme.colors.primary.copy(alpha = 0.12f)
                        else MaterialTheme.colors.surface
                    )
                ) {
                    Text(label)
                }
            }
        }
    }
}

@Composable
fun ReminderItem(reminder: Reminder, onDelete: () -> Unit, onToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(reminder.title, style = MaterialTheme.typography.subtitle1)
                Text("Каждые ${reminder.intervalMinutes} минут", style = MaterialTheme.typography.body2)
            }
            Column(horizontalAlignment = Alignment.End) {
                Switch(checked = reminder.enabled, onCheckedChange = { onToggle() })
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onDelete) { Text("Удалить") }
            }
        }
    }
}

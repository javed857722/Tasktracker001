package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktracker001.data.Task

data class TaskSuggestion(val title: String, val description: String)

object AIAssistant {

    private val stopWords = setOf("a", "an", "and", "the", "in", "on", "of", "for", "to", "with", "is", "are", "was", "were", "it", "i", "you", "he", "she", "they", "we")

    private val suggestionMap = mapOf(
        "buy" to listOf(
            TaskSuggestion("Buy groceries", "Milk, bread, eggs, and cheese"),
            TaskSuggestion("Buy a birthday gift", "For Sarah's birthday party next week"),
        ),
        "work" to listOf(
            TaskSuggestion("Finish report", "Complete the Q3 sales report"),
            TaskSuggestion("Schedule meeting", "Arrange a meeting with the marketing team"),
            TaskSuggestion("Reply to emails", "Clear the inbox"),
        ),
        "home" to listOf(
            TaskSuggestion("Clean the house", "Vacuum, dust, and do laundry"),
            TaskSuggestion("Fix the leaky faucet", "In the kitchen sink"),
            TaskSuggestion("Mow the lawn", ""),
        ),
        "health" to listOf(
            TaskSuggestion("Go for a run", "30 minutes run in the park"),
            TaskSuggestion("Schedule a doctor's appointment", ""),
            TaskSuggestion("Meditate", "10 minutes of mindfulness meditation"),
        ),
        "call" to listOf(
            TaskSuggestion("Call mom", ""),
            TaskSuggestion("Call the bank", "To check on the loan application"),
        ),
        "book" to listOf(
            TaskSuggestion("Book flight", "Book a flight to New York"),
            TaskSuggestion("Book hotel", "For the upcoming vacation"),
        ),
        "pay" to listOf(
            TaskSuggestion("Pay bills", "Pay the electricity and internet bills"),
            TaskSuggestion("Pay rent", ""),
        )
    )

    fun getSuggestions(tasks: List<Task>): List<TaskSuggestion> {
        val allWords = tasks.flatMap { (it.title + " " + it.description).split(" ") }
            .map { it.lowercase().trim().filter { char -> char.isLetter() } }
            .filter { it.isNotBlank() && it !in stopWords }

        if (allWords.isEmpty()) {
            return listOf(
                TaskSuggestion("Buy groceries", "Milk, bread, eggs, and cheese"),
                TaskSuggestion("Finish report", "Complete the Q3 sales report"),
                TaskSuggestion("Schedule meeting", "Arrange a meeting with the marketing team")
            )
        }

        val wordFrequencies = allWords.groupingBy { it }.eachCount()
        
        val topKeywords = wordFrequencies.entries
            .sortedByDescending { it.value }
            .take(3)
            .map { it.key }

        val suggestions = topKeywords.flatMap { keyword ->
            suggestionMap.entries.find { (key, _) -> keyword.contains(key) || key.contains(keyword) }?.value ?: emptyList()
        }.distinct()
        
        if (suggestions.isEmpty()) {
             return listOf(
                TaskSuggestion("Buy groceries", "Milk, bread, eggs, and cheese"),
                TaskSuggestion("Finish report", "Complete the Q3 sales report"),
                TaskSuggestion("Schedule meeting", "Arrange a meeting with the marketing team")
            )
        }

        return suggestions
    }
}

@Composable
fun TaskSuggestions(taskViewModel: TaskViewModel, onSuggestionSelected: (TaskSuggestion) -> Unit) {
    val tasks by taskViewModel.tasks.collectAsState()
    val suggestions = AIAssistant.getSuggestions(tasks)

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("AI Suggestions")
        LazyRow {
            items(suggestions) { suggestion ->
                Button(onClick = { onSuggestionSelected(suggestion) }, modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(suggestion.title)
                }
            }
        }
    }
}
package com.example.smtcvningar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smtcvningar.ui.theme.SMTCÖvningarTheme

data class Exercise(
    val title: String,
    val description: String,
    val steps: List<String>
)

val exercises = listOf(
    Exercise(
        title = "Flytta fokus",
        description = "Öva på att flytta uppmärksamheten mellan olika saker.",
        steps = listOf(
            "Sitt bekvämt.",
            "Lägg märke till ett ljud långt bort.",
            "Lägg märke till ett ljud nära dig.",
            "Titta på ett föremål framför dig.",
            "Lägg märke till känslan i dina fötter.",
            "Flytta fokus mellan ljud, syn och kropp."
        )
    ),
    Exercise(
        title = "Lägg märke till tanken",
        description = "Öva på att notera en tanke utan att fastna i den.",
        steps = listOf(
            "Sitt lugnt och bekvämt.",
            "Lägg märke till en tanke som dyker upp.",
            "Säg tyst för dig själv: det där var en tanke.",
            "Låt tanken finnas utan att analysera den.",
            "Flytta tillbaka fokus till det du gör."
        )
    ),
    Exercise(
        title = "Skjut upp grubblande",
        description = "Öva på att inte gå in i oro direkt.",
        steps = listOf(
            "Lägg märke till att du börjar grubbla.",
            "Säg till dig själv att du kan ta det senare.",
            "Bestäm en tid då du får tänka på det.",
            "Återgå till det du höll på med.",
            "Öva på att låta tanken vänta."
        )
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SMTCÖvningarTheme {
                var showExercises by remember { mutableStateOf(false) }
                var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
                var showReflection by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when {
                        showReflection && selectedExercise != null -> {
                            ReflectionScreen(
                                exercise = selectedExercise!!,
                                modifier = Modifier.padding(innerPadding),
                                onDoneClick = {
                                    showReflection = false
                                    selectedExercise = null
                                }
                            )
                        }

                        selectedExercise != null -> {
                            ExerciseDetailScreen(
                                exercise = selectedExercise!!,
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = { selectedExercise = null },
                                onCompleteClick = { showReflection = true }
                            )
                        }

                        showExercises -> {
                            ExercisesScreen(
                                modifier = Modifier.padding(innerPadding),
                                onExerciseClick = { exercise ->
                                    selectedExercise = exercise
                                }
                            )
                        }

                        else -> {
                            HomeScreen(
                                modifier = Modifier.padding(innerPadding),
                                onStartClick = { showExercises = true }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MCT Övningar",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Lär dig grunderna i metakognitiv terapi och öva enkla moment steg för steg.",
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp),
            fontSize = 18.sp
        )

        Button(onClick = onStartClick) {
            Text("Börja")
        }
    }
}

@Composable
fun ExercisesScreen(
    modifier: Modifier = Modifier,
    onExerciseClick: (Exercise) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Övningar",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        exercises.forEach { exercise ->
            Button(
                onClick = { onExerciseClick(exercise) },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = exercise.title,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = exercise.description)
                }
            }
        }
    }
}

@Composable
fun ExerciseDetailScreen(
    exercise: Exercise,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = exercise.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = exercise.description,
            fontSize = 18.sp
        )

        val focusSteps = listOf(
            "Sitt bekvämt.",
            "Lägg märke till ett ljud långt bort.",
            "Lägg märke till ett ljud nära dig.",
            "Titta på ett föremål framför dig.",
            "Lägg märke till känslan i dina fötter.",
            "Flytta fokus mellan ljud, syn och kropp."
        )

        var currentStep by remember { mutableStateOf(0) }

        if (exercise.title == "Flytta fokus") {
            Text(
                text = "Steg ${currentStep + 1} av ${focusSteps.size}",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = focusSteps[currentStep],
                fontSize = 20.sp
            )

            if (currentStep < focusSteps.lastIndex) {
                Button(onClick = { currentStep++ }) {
                    Text("Nästa")
                }
            } else {
                Button(onClick = onCompleteClick) {
                    Text("Klar")
                }
            }

            Button(onClick = onBackClick) {
                Text("Tillbaka")
            }
        } else {
            Text("Övningsinnehåll kommer snart.")

            Button(onClick = onBackClick) {
                Text("Tillbaka")
            }
        }
    }
}

@Composable
fun ReflectionScreen(
    exercise: Exercise,
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit
) {
    var reflection by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Reflektion",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Hur kändes övningen \"${exercise.title}\"?",
            fontSize = 18.sp
        )

        Button(onClick = { reflection = "Lätt" }) {
            Text("Lätt")
        }

        Button(onClick = { reflection = "Ganska lätt" }) {
            Text("Ganska lätt")
        }

        Button(onClick = { reflection = "Svårt" }) {
            Text("Svårt")
        }

        if (reflection.isNotEmpty()) {
            Text(
                text = "Du valde: $reflection",
                fontWeight = FontWeight.Bold
            )

            Button(onClick = onDoneClick) {
                Text("Tillbaka till övningar")
            }
        }
    }
}
package com.singularity.trainingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.singularity.trainingapp.core.navigation.ChatList
import com.singularity.trainingapp.core.navigation.FeedHome
import com.singularity.trainingapp.core.navigation.ProfileMe
import com.singularity.trainingapp.core.navigation.TabChat
import com.singularity.trainingapp.core.navigation.TabFeed
import com.singularity.trainingapp.core.navigation.TabProfile
import com.singularity.trainingapp.core.navigation.TabWorkout
import com.singularity.trainingapp.core.ui.TestScreen
import com.singularity.trainingapp.core.ui.TrainingBottomBarHost
import com.singularity.trainingapp.features.workout.workoutGraph
import com.singularity.trainingapp.ui.theme.TrainingAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainingAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        TrainingBottomBarHost(navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = TabWorkout,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        workoutGraph(navController)

                        navigation<TabFeed>(startDestination = FeedHome) {
                            composable<FeedHome> {
                                TestScreen("Feed")
                            }
                        }

                        navigation<TabChat>(startDestination = ChatList) {
                            composable<ChatList> { TestScreen("Chats list") }
                        }
                        navigation<TabProfile>(startDestination = ProfileMe) {
                            composable<ProfileMe> { TestScreen("Profile") }
                        }

                    }
                }
            }
        }
    }
}


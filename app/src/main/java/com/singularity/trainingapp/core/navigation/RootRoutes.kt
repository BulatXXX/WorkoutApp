package com.singularity.trainingapp.core.navigation

import kotlinx.serialization.Serializable


interface WorkoutAppRoute
interface TabRoute : WorkoutAppRoute
//Auth
@Serializable data object Auth // auth graph container
@Serializable data object SignIn                                               : WorkoutAppRoute
@Serializable data object SignUp                                               : WorkoutAppRoute
@Serializable data object PasswordRecovery                                     : WorkoutAppRoute
@Serializable data object PasswordReset                                        : WorkoutAppRoute
//Tabs
@Serializable data object TabWorkout                                          : TabRoute
@Serializable data object TabFeed                                              : TabRoute
@Serializable data object TabChat                                              : TabRoute
@Serializable data object TabProfile                                           : TabRoute

//Trainings
@Serializable data object WorkoutList                                         : WorkoutAppRoute

@Serializable
data object ScheduleRoute : WorkoutAppRoute
@Serializable data class WorkoutDetail(val id: String)                        : WorkoutAppRoute
//some more screens for training to count exercises

//Exercises(Admin)
@Serializable
data object ExercisesListRoute : WorkoutAppRoute

//Feed (there will be posts of your friends with training results and stats)
@Serializable data object FeedHome                                             : WorkoutAppRoute
@Serializable data class FeedPost(val postId: String)                          : WorkoutAppRoute
@Serializable data object FeedWorkoutStats                                    : WorkoutAppRoute

//Chat (will be implemented soon, to connect with friends)
@Serializable data object ChatList                                             : WorkoutAppRoute
@Serializable data class ChatDetailed(val chatId: String)                      : WorkoutAppRoute

//Profile
@Serializable data object ProfileMe                                            : WorkoutAppRoute
@Serializable data object ProfileSettings                                      : WorkoutAppRoute
/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.unscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.unscramble.ui.GameScreen
import com.example.unscramble.ui.GameViewModel
import com.example.unscramble.ui.theme.UnscrambleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnscrambleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val gameViewModel : GameViewModel by viewModels()
                    val gameUiState by gameViewModel.uiState.collectAsState()
                    val userGuess = gameViewModel.userGuess
                    val updateUserGuess = { _userGuess: String -> gameViewModel.updateUserGuess(_userGuess) }
                    val checkUserGuess = { gameViewModel.checkUserGuess() }
                    val skipWord = { gameViewModel.skipWord() }
                    val resetGame = gameViewModel::resetGame
                    GameScreen(
                        gameUiState = gameUiState,
                        userGuess = userGuess,
                        updateUserGuess = updateUserGuess,
                        checkUserGuess = checkUserGuess,
                        skipWord = skipWord,
                        resetGame = resetGame
                    )
                }
            }
        }
    }
}

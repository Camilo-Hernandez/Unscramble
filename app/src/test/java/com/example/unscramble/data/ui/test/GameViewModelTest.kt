package com.example.unscramble.data.ui.test

import androidx.core.graphics.createBitmap
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }

    private lateinit var gameViewModel: GameViewModel

    @Before
    fun setUp() {
        gameViewModel = GameViewModel()
    }

    @Test
    fun `GIVEN an instance of a GameViewModel WHEN it first created THEN the initial state is correct`() {
        with(gameViewModel.uiState.value) {
            assertFalse(isGameOver)
            assertSame(0, score)
            assertFalse(isGuessedWordWrong)
            assertSame(1, currentWordCount)
            val unScrambledWord = getUnscrambledWord(currentScrambledWord)
            assertTrue(allWords.contains(unScrambledWord))
            assertNotEquals(unScrambledWord, currentScrambledWord)
        }
    }

    @Test
    fun `GIVEN the correct word WHEN is guessed THEN the score is updated and the error flag unset`() {
        gameViewModel.run {
            val correctPlayerWord = getUnscrambledWord(uiState.value.currentScrambledWord)
            updateUserGuess(correctPlayerWord)
            checkUserGuess()
            // Assert that checkUserGuess() method updates isGuessedWordWrong is updated correctly
            assertFalse(uiState.value.isGuessedWordWrong)
            // Assert that score is updated correctly
            assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, uiState.value.score)
        }
    }

    @Test
    fun `GIVEN an incorrect word WHEN is guessed THEN the error flag is true and the score is the same`() {
        val incorrectWord = "and"
        val currentGameUiState = gameViewModel.uiState.value
        gameViewModel.updateUserGuess(incorrectWord)
        gameViewModel.checkUserGuess()
        val posGameUiState = gameViewModel.uiState.value

        assertSame(currentGameUiState.score, posGameUiState.score)
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertTrue(posGameUiState.isGuessedWordWrong)
    }

    @Test
    fun `GIVEN all words are guessed THEN the UI state is updated correctly as a game over`() {
        repeat(MAX_NO_OF_WORDS) {
            val expectedScore = SCORE_INCREASE * it
            val currentGameUiState = gameViewModel.uiState.value
            val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            assertEquals(expectedScore, currentGameUiState.score)
            assertFalse(currentGameUiState.isGuessedWordWrong)
            gameViewModel.updateUserGuess(correctPlayerWord)
            gameViewModel.checkUserGuess()
        }
        assertTrue(gameViewModel.uiState.value.isGameOver)
        assertSame(MAX_NO_OF_WORDS, gameViewModel.uiState.value.currentWordCount)
    }

    @Test
    fun `WHEN a word is skipped THEN the scored is unchanged and the word count is increased by one`() {
        // We play a turn correctly
        gameViewModel.uiState.value.run {
            getUnscrambledWord(this.currentScrambledWord).also {
                gameViewModel.updateUserGuess(it)
                gameViewModel.checkUserGuess()
            }
        }

        gameViewModel.uiState.value.let { posGameUiState ->
            // Then skip another turn
            val lastWordCount = posGameUiState.currentWordCount
            gameViewModel.skipWord()
            assertSame(lastWordCount, posGameUiState.currentWordCount)
            assertSame(SCORE_AFTER_FIRST_CORRECT_ANSWER, posGameUiState.score)

            // Then check the score hasn't changed
            val posX2GameUiState = gameViewModel.uiState.value
            assertSame(SCORE_AFTER_FIRST_CORRECT_ANSWER, posX2GameUiState.score)
            assertSame(lastWordCount + 1, posX2GameUiState.currentWordCount)
        }
    }

    @Test
    fun testShuffleCurrentWord() {
        val word = "hola"
        val shuffledWord = gameViewModel.shuffleCurrentWord(word)
        run {
            val tempWord = word.toCharArray()
            // Scramble the word
            tempWord.shuffle()
            while (String(tempWord) == word) {
                tempWord.shuffle()
            }
            assertNotEquals(shuffledWord, String(tempWord))
        }
    }

}
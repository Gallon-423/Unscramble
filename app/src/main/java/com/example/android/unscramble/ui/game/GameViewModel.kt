package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


//ViewModel 是一个抽象类，因此您需要对它进行扩展才能在应用中使用它。
class GameViewModel : ViewModel() {
    //在 init 代码块上方添加一个名为 getNextWord() 的新 private 方法，该方法不带参数也不返回任何内容。
    //从 allWordsList 中获取一个随机单词并将其赋值给 currentWord。
    /*
     * Updates currentWord and currentScrambledWord with the next word.
     */

    private var wordsList: MutableList<String> = mutableListOf()
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score
    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount
    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord:MutableLiveData<String>
        get() = _currentScrambledWord

    /*
     * Returns true if the current word count is less than MAX_NO_OF_WORDS.
     * Updates the next word.
     */
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }
    private fun getNextWord() {
        _currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = _currentWordCount.value?.inc()
            wordsList.add(currentWord)
        }
    }
    //初始代码块
    //Kotlin 提供了初始化式块（也称为 init 块），作为对象实例初始化期间所需的初始设置代码的位置。
    init {
        Log.d("GameFragment", "GameViewModel 创建!")
        getNextWord()
    }



    //在 GameViewModel, 中，添加一个类型为 MutableList<String>、
    // 名为 wordsList 的新类变量，用于存储游戏中所用单词的列表，以避免重复。
    private lateinit var _currentWord: String
    val currentWord:String
        get() = _currentWord
    //添加名为 currentWord 的另一个类变量，用于存储玩家正在尝试理顺的单词。
    //请使用 lateinit 关键字，因为您稍后才会初始化此属性。



    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }
    /*
     * Re-initializes the game data to restart the game.
     */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }
    override fun onCleared() {
        Log.d("GameFragment", "GameViewModel 销毁!")
        super.onCleared()
    }
}
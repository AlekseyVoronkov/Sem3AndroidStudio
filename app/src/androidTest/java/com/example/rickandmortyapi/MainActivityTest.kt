package com.example.rickandmortyapi

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rickandmortyapi.Activity.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun recyclerViewShouldBeDisplayed() {
        // Запускаем MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // Проверяем, что RecyclerView отображается
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun snackbarShouldDisplayErrorMessage() {
        // Запускаем MainActivity
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Получаем доступ к активности
            scenario.onActivity { activity ->
                // Симулируем ошибку через ViewModel активности
                activity.viewModel.setErrorMessage("Ошибка сети: Timeout")
            }

            // Проверяем, что Snackbar отображается
            onView(withText("Ошибка сети: Timeout")).check(matches(isDisplayed()))
        }
    }
}
package com.geekbrains.tests.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.geekbrains.tests.*
import com.geekbrains.tests.RESOURCE_ID_SEARCH_EDIT_TEXT
import com.geekbrains.tests.NUMBER_OF_RESULTS_REAL
import com.geekbrains.tests.TEST_NUMBER_OF_RESULTS_ZERO
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {

    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()

    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName

    @Before
    fun setup() {
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()

        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        //Мы уже проверяли Интент на null в предыдущем тесте, поэтому допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)//Чистим бэкстек от запущенных ранее Активити
        context.startActivity(intent)

        //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT_START_ACTIVITY)
    }

    //Убеждаемся, что приложение открыто. Для этого достаточно найти на экране любой элемент
    //и проверить его на null
    @Test
    fun test_MainActivityIsStarted() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, RESOURCE_ID_SEARCH_EDIT_TEXT))
        //Проверяем на null
        Assert.assertNotNull(editText)
    }

    //Убеждаемся, что поиск работает как ожидается
    @Test
    fun test_SearchIsPositive() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, RESOURCE_ID_SEARCH_EDIT_TEXT))
        //Устанавливаем значение
        editText.text = STRING_SEARCH_QUERY
        //Отправляем запрос через Espresso
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText))
            .perform(ViewActions.pressImeActionButton())

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что сервер вернул ответ с какими-то данными, то есть запрос отработал.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, RESOURCE_ID_TOTAL_COUNT_TEXT_VIEW)),
                TIMEOUT_SEARCH_ON_SERVER
            )
        //Убеждаемся, что сервер вернул корректный результат. Обратите внимание, что количество
        //результатов может варьироваться во времени, потому что количество репозиториев постоянно меняется.
        Assert.assertEquals(changedText.text.toString(), NUMBER_OF_RESULTS_REAL)
    }

    //Убеждаемся, что DetailsScreen открывается
    @Test
    fun test_OpenDetailsScreen() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(packageName, RESOURCE_ID_TO_DETAIL_ACTIVITY_BUTTON)
        )
        //Кликаем по ней
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, RESOURCE_ID_TOTAL_COUNT_TEXT_VIEW)),
                TIMEOUT_SEARCH_ON_SERVER
            )
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        //Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        //Чтобы проверить отображение определенного количества репозиториев,
        //вам в одном и том же методе нужно отправить запрос на сервер и открыть DetailsScreen.
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_ZERO)
    }
}

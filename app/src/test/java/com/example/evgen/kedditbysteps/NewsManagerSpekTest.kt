package com.example.evgen.kedditbysteps

import com.example.evgen.kedditbysteps.api.*
import com.example.evgen.kedditbysteps.commons.RedditNews
import com.example.evgen.kedditbysteps.features.news.NewsManager
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.Assert.*
import org.mockito.ArgumentMatchers.anyString

import org.mockito.Mockito.`when`
import retrofit2.Call
import retrofit2.Response
import rx.observers.TestSubscriber
import java.util.*

class NewsManagerSpekTest: Spek({

    group("variables") {

        var i = 0;

        context(" a test variables") {
            beforeEachTest { i = 0 }

            describe("Проверка на ноль") {

                it("i is zero") {
                    assertEquals(0, i)
                }
                it("i not zero") {
                    i++
                    assertNotEquals(0, i)
                }
            }
        }
    }

    context("a NewsManager") {
        var testSub = TestSubscriber<RedditNews>()
        var apiMock = mock<NewsAPI>()
        var callMock = mock<Call<RedditNewsResponse>>()

        beforeEachTest {
            testSub = TestSubscriber<RedditNews>()
            apiMock = mock<NewsAPI>()
            callMock = mock<Call<RedditNewsResponse>>()
            `when`(apiMock.getNews(anyString(), anyString())).thenReturn(callMock)
        }

        describe("service returns something") {
            beforeEachTest {
                // prepare
                val redditNewsResponse = RedditNewsResponse(RedditDataResponse(listOf(), null, null))
                val response = Response.success(redditNewsResponse)

                `when`(callMock.execute()).thenReturn(response)

                // call
                val newsManager = NewsManager(apiMock)
                newsManager.getNews("").subscribe(testSub)
            }

            it("should receive something and no errors") {
                testSub.assertNoErrors()
                testSub.assertValueCount(1)
                testSub.assertCompleted()
            }
        }

        describe("service returns just one news") {
            val newsData = RedditNewsDataResponse(
                    "author",
                    "title",
                    10,
                    Date().time,
                    "thumbnail",
                    "url"
            )
            beforeEachTest {
                // prepare
                val newsResponse = RedditChildrenResponse(newsData)
                val redditNewsResponse = RedditNewsResponse(RedditDataResponse(listOf(newsResponse), null, null))
                val response = Response.success(redditNewsResponse)

                `when`(callMock.execute()).thenReturn(response)

                // call
                val newsManager = NewsManager(apiMock)
                newsManager.getNews("").subscribe(testSub)
            }

            it("should process only one news successfully") {
                testSub.assertNoErrors()
                testSub.assertValueCount(1)
                testSub.assertCompleted()

                assert(testSub.onNextEvents[0].news[0].author == newsData.author)
                assert(testSub.onNextEvents[0].news[0].title == newsData.title)
            }
        }

        describe("service returns a 500 error") {
            beforeEachTest {
                // prepare
                val responseError = Response.error<RedditNewsResponse>(500,
                        ResponseBody.create(MediaType.parse("application/json"), ""))

                `when`(callMock.execute()).thenReturn(responseError)

                // call
                val newsManager = NewsManager(apiMock)
                newsManager.getNews("").subscribe(testSub)
            }

            it("should receive an onError message") {
                assert(testSub.onErrorEvents.size == 1)
            }
        }
    }
})
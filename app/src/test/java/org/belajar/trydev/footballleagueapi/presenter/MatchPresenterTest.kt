package org.belajar.trydev.footballleagueapi.presenter

import com.google.gson.Gson
import org.belajar.trydev.footballleagueapi.TestContextProvider
import org.belajar.trydev.footballleagueapi.api.ApiRepository
import org.belajar.trydev.footballleagueapi.api.TheSportDBApi
import org.belajar.trydev.footballleagueapi.model.Event
import org.belajar.trydev.footballleagueapi.model.EventResponse
import org.belajar.trydev.footballleagueapi.view.MatchView
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MatchPresenterTest {

    @Mock
    private
    lateinit var view:MatchView

    @Mock
    private
    lateinit var gson:Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    private lateinit var presenter: MatchPresenter

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        presenter = MatchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun getPastEvent() {
        val eventId = "4328"
        val event:MutableList<Event> = mutableListOf()
        val response = EventResponse(event)

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getPastEvent(eventId)),
                EventResponse::class.java
        )).thenReturn(response)

        presenter.getPastEvent(eventId)

        verify(view).showLeagueList(event)
        verify(view).hideLoading()
    }

    @Test
    fun getNextEvent(){
        val eventId = "4328"
        val event:MutableList<Event> = mutableListOf()
        val response = EventResponse(event)

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getNextEvent(eventId)),
                EventResponse::class.java
        )).thenReturn(response)

        presenter.getNextEvent(eventId)

        verify(view).showLeagueList(event)
        verify(view).hideLoading()
    }
}
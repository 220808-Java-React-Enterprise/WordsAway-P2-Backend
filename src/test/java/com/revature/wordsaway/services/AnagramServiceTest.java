package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.responses.AnagramResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AnagramServiceTest {

    private AnagramService anagramService;
    private RestTemplate mockRestTemplate;

    @BeforeEach
    public void setup(){
        mockRestTemplate = mock(RestTemplate.class);
        anagramService = new AnagramService(mockRestTemplate);
    }

    @AfterEach
    public void setdown(){
        anagramService = null;
        mockRestTemplate = null;
    }

    @Test
    public void test_getBest_succeed(){
        when(mockRestTemplate.getForObject("http://www.anagramica.com/best/test", String.class)).thenReturn("test");
        String result = anagramService.getBest("test");
        verify(mockRestTemplate, times(1)).getForObject("http://www.anagramica.com/best/test", String.class);
        assertEquals(result, "test");
    }

    @Test
    public void test_getBest_fail(){
        when(mockRestTemplate.getForObject("http://www.anagramica.com/best/test", String.class)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        String result = anagramService.getBest("test");
        verify(mockRestTemplate, times(1)).getForObject("http://www.anagramica.com/best/test", String.class);
        assertEquals(result, "");
    }

    @Test
    public void test_getAll_succeed(){
        when(mockRestTemplate.getForObject("http://www.anagramica.com/all/test", String.class)).thenReturn("test");
        String result = anagramService.getAll("test");
        verify(mockRestTemplate, times(1)).getForObject("http://www.anagramica.com/all/test", String.class);
        assertEquals(result, "test");
    }

    @Test
    public void test_getAll_fail(){
        when(mockRestTemplate.getForObject("http://www.anagramica.com/all/test", String.class)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        String result = anagramService.getAll("test");
        verify(mockRestTemplate, times(1)).getForObject("http://www.anagramica.com/all/test", String.class);
        assertEquals(result, "");
    }

    @Test
    public void test_isWord_succeed(){
        AnagramResponse mockAnagramResponse = mock(AnagramResponse.class);
        when(mockAnagramResponse.getFound()).thenReturn(1);
        when(mockRestTemplate.getForObject("http://www.anagramica.com/lookup/test", AnagramResponse.class)).thenReturn(mockAnagramResponse);
        Boolean result = anagramService.isWord("test");
        verify(mockRestTemplate, times(1)).getForObject("http://www.anagramica.com/lookup/test", AnagramResponse.class);
        assertEquals(result, true);
    }

    @Test
    public void test_isWord_fail(){
        AnagramResponse mockAnagramResponse = mock(AnagramResponse.class);
        when(mockAnagramResponse.getFound()).thenReturn(0);
        when(mockRestTemplate.getForObject("http://www.anagramica.com/lookup/test", AnagramResponse.class)).thenReturn(mockAnagramResponse);
        Boolean result = anagramService.isWord("test");
        verify(mockRestTemplate, times(1)).getForObject("http://www.anagramica.com/lookup/test", AnagramResponse.class);
        assertEquals(result, false);
    }

    @Test
    public void test_getAllList_succeed(){
        List<String> result = anagramService.getAllList("test", "", 4);
        assertEquals(result, new ArrayList<>(Arrays.asList("SETT", "STET", "TEST", "TETS", "EST", "SET", "TES", "TET")));
    }

    @Test
    public void test_getAllList_fail(){
        List<String> result = anagramService.getAllList("va", "", 4);
        assertEquals(result, new ArrayList<>());
    }
}
package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.reporting.service.search.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SearchControllerTest extends BaseControllerTestRunner {

    private static final String PATH = "/search";

    private static final RawJson RESULT = new RawJson("{\"a\": 117}");

    @Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(controller);
        Environment.setCurrentUser(Generator.getPerson());
    }

    @Test
    public void fullTextSearchPassesExpressionToSearchService() throws Exception {
        final String expression = "search for me";
        when(searchService.fullTextSearch(expression)).thenReturn(RESULT);
        final MvcResult result = mockMvc
                .perform(get(PATH).param(SearchController.EXPRESSION_PARAM, expression)
                                  .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        final RawJson content = new RawJson(result.getResponse().getContentAsString());
        assertEquals(RESULT, content);
        verify(searchService).fullTextSearch(expression);
    }

    @Test
    public void throwsBadRequestWhenSearchExpressionIsMissing() throws Exception {
        mockMvc.perform(get(PATH).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
        verify(searchService, never()).fullTextSearch(anyString());
    }
}

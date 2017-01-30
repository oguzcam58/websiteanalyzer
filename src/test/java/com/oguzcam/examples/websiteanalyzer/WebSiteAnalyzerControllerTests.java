package com.oguzcam.examples.websiteanalyzer;

import com.oguzcam.examples.message.BootstrapMessage;
import org.hamcrest.Matchers;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.oguzcam.examples.message.BootstrapMessage.BSMessageType.DANGER;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Jsoup.class)
public class WebSiteAnalyzerControllerTests {

    private MockMvc mockMvc;
    @Spy
    private WebSiteAnalyzerUtils utils = new WebSiteAnalyzerUtils();
    @InjectMocks
    private WebSiteAnalyzerController webSiteAnalyzerController;

    @Before
    public void setup() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");

        MockitoAnnotations.initMocks(this);

        this.mockMvc = standaloneSetup(webSiteAnalyzerController).setViewResolvers(viewResolver).build();
    }

    private void setupJsoupMockForGoogleWebSite() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource("google.html");
        File file = new File(resource.getFile());
        Document doc = Jsoup.parse(file, "UTF-8");

        Connection connectionMock = mock(Connection.class);
        // Mock static methods of Jsoup in order to avoid internet access
        // which is not necessary in Unit Tests
        PowerMockito.mockStatic(Jsoup.class);
        PowerMockito.when(Jsoup.connect(any(String.class))).thenReturn(connectionMock);

        when(connectionMock.timeout(any(Integer.class))).thenReturn(connectionMock);
        when(connectionMock.get()).thenReturn(doc);
        doReturn(5).when(utils).countLinks(any(Element.class));
        doReturn(4).when(utils).countExternalLinks(any(Element.class), any(String.class));
    }

    @Test
    public void submitSuccess() throws Exception {
        setupJsoupMockForGoogleWebSite();
        String urlString = "http://google.com";
        this.mockMvc.perform(
                post("/")
                        .param("urlString", urlString)
                        .sessionAttr("webSiteBean", new WebSiteBean()))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("webSiteBean", hasProperty("urlString", is(urlString))))
                .andExpect(model().attribute("webSiteBean", hasProperty("htmlVersion", is("HTML 5"))))
                .andExpect(model().attribute("webSiteBean", hasProperty("title", is("Google"))))
                .andExpect(status().isOk());
    }

    @Test
    public void submitFail() throws Exception {
        when(utils.validator(eq("http://google"))).thenReturn(false);
        this.mockMvc.perform(
                post("/")
                        .param("urlString", "http://google")
                        .sessionAttr("webSiteBean", new WebSiteBean()))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("webSiteBean", hasProperty("urlString", is("http://"))))
                .andExpect(model().attribute("messages", Matchers.hasItem(new BootstrapMessage(DANGER, "Specified url is not valid, please specify a valid URL."))))
                .andExpect(status().isOk());
    }

    @Test
    public void performGetDocument() throws Exception {
        setupJsoupMockForGoogleWebSite();
        List<BootstrapMessage> list = new ArrayList<>();
        new WebSiteAnalyzerController().getDocument("http://google.com", list);
        assertEquals(0, list.size());
    }

    @Test
    public void performGetDocumentWithUnknownHost() {
        List<BootstrapMessage> list = new ArrayList<>();
        new WebSiteAnalyzerController().getDocument("http://google2.com", list);
        assertEquals(1, list.size());
        assertEquals(new BootstrapMessage(DANGER, "Couldn't reach to the URL."), list.get(0));
    }

    @Test
    public void performGetDocumentWithWrongHtml() {
        List<BootstrapMessage> list = new ArrayList<>();
        new WebSiteAnalyzerController().getDocument("http://alican", list);
        assertEquals(1, list.size());
        assertEquals(new BootstrapMessage(DANGER, "Couldn't reach to the URL."), list.get(0));
    }
}
package com.oguzcam.examples.websiteanalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Test for URLAnalyzerUtils
 */
public class WebSiteAnalyzerUtilsTests {

    private WebSiteAnalyzerUtils utils;

    @Before
    public void setup() {
        utils = new WebSiteAnalyzerUtils();
    }

    @Test
    public void performLocalhostValidURL() {
        assertTrue(utils.validator("http://localhost:8080/examples-home-project/"));
    }

    @Test
    public void performValidURLs() {
        assertTrue(utils.validator("http://google.com"));
        assertTrue(utils.validator("http://www.google.com"));
        assertTrue(utils.validator("http://yahoo.com"));
        assertTrue(utils.validator("http://facebook.com"));
        assertTrue(utils.validator("https://google.com"));
        assertTrue(utils.validator("https://facebook.com"));
        assertTrue(utils.validator("https://www.spiegel.de/meinspiegel/login.html"));
        assertTrue(utils.validator("https://github.com/login"));
    }

    @Test
    public void performInvalidURLs() {
        assertFalse(utils.validator("google.com"));
        assertFalse(utils.validator("alican.com"));
        assertFalse(utils.validator("ftp://yahoo.com"));
        assertFalse(utils.validator(".com"));
        assertFalse(utils.validator("sttp://facebook.com"));
        assertFalse(utils.validator("ftp://facebook.com"));
        assertFalse(utils.validator(""));
        assertFalse(utils.validator(null));
    }

    @Test
    public void performGetDomainWithValidInputs() {
        assertEquals("//www.google.com", utils.getDomain("http://www.google.com"));
        assertEquals("//www.google.com.tr", utils.getDomain("https://www.google.com.tr/?gfe_rd=cr&ei=LAQ8WI7XIcv38Afw3Ir4AQ#q=examples"));
        assertEquals("//www.spiegel.de", utils.getDomain("https://www.spiegel.de/meinspiegel/login.html"));
        assertEquals("//github.com", utils.getDomain("https://github.com/login"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void performGetDomainWithNullInput() {
        utils.getDomain(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void performGetDomainWithEmptyInput() {
        utils.getDomain("");
    }

    @Test
    public void performHTMLVersion() {
        Document doc = Jsoup.parse("<!DOCTYPE html><html><body>Content</body></html>");
        assertEquals("HTML 5", utils.getHTMLVersion(doc));

        doc = Jsoup.parse("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><body>Content</body></html>");
        assertEquals("HTML 4.01", utils.getHTMLVersion(doc));
    }

    @Test(expected = IllegalArgumentException.class)
    public void performHTMLVersionWithNullInput() {
        utils.getHTMLVersion(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void performHTMLVersionWithEmptyInput() {
        Document doc = new Document("");
        utils.getHTMLVersion(doc);
    }

    @Test
    public void performCountHeadings() {
        String htmlString = "<!DOCTYPE html><html><body>"
                            + "<h1>H1 1</h1>"
                            + "<h2>H2 1</h2><h2>H2 2</h2>"
                            + "<h3>H3 1</h3><h3>H3 2</h3><h3>H3 3</h3>"
                            + "<h4>H4 1</h4><h4>H4 2</h4><h4>H4 3</h4><h4>H4 4</h4>"
                            + "<h5>H5 1</h5><h5>H5 2</h5><h5>H5 3</h5><h5>H5 4</h5><h5>H5 5</h5>"
                            + "<h6>H6 1</h6><h6>H6 2</h6><h6>H6 3</h6><h6>H6 4</h6><h6>H6 5</h6><h6>H6 6</h6>"
                            + "</body></html>";

        Document doc = Jsoup.parse(htmlString);
        int[] headings = utils.countHeadings(doc.body());
        assertEquals(1, headings[0]);
        assertEquals(2, headings[1]);
        assertEquals(3, headings[2]);
        assertEquals(4, headings[3]);
        assertEquals(5, headings[4]);
        assertEquals(6, headings[5]);
    }

    @Test
    public void performCountZeroHeadings() {
        String htmlString = "<!DOCTYPE html><html><body></body></html>";

        Document doc = Jsoup.parse(htmlString);
        int[] headings = utils.countHeadings(doc.body());
        assertEquals(0, headings[0]);
        assertEquals(0, headings[1]);
        assertEquals(0, headings[2]);
        assertEquals(0, headings[3]);
        assertEquals(0, headings[4]);
        assertEquals(0, headings[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void performCountHeadingsWithNullInput() {
        utils.countHeadings(null);
    }

    @Test
    public void performCountLinks() {
        String htmlString = "<!DOCTYPE html><html><body>"
                    + "<a href=\"#Topic1\">Topic 1</a>"
                    + "<a href=\"#Topic2\">Topic 2</a>"
                    + "<a href=\"/nextPage\">Next Page</a>"
                    + "</body></html>";

        Document doc = Jsoup.parse(htmlString);
        assertEquals(3,utils.countLinks(doc.body()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void performCountLinksWithNullInput() {
        utils.countLinks(null);
    }

    @Test
    public void performCountZeroExternalLinks() {
        String htmlString = "<!DOCTYPE html><html><body>"
                            + "<a href=\"#Topic1\">Topic 1</a>"
                            + "<a href=\"#Topic2\">Topic 2</a>"
                            + "<a href=\"/nextPage\">Next Page</a>"
                            + "</body></html>";

        Document doc = Jsoup.parse(htmlString);
        assertEquals(0,utils.countExternalLinks(doc.body(), "//google.com"));
    }

    @Test
    public void performCountExternalLinks() {
        String htmlString = "<!DOCTYPE html><html><body>"
                            + "<a href=\"//google.com\">Google</a>"
                            + "<a href=\"//yahoo.com\">Yahoo</a>"
                            + "<a href=\"//twitter.com\">Twitter</a>"
                            + "</body></html>";

        Document doc = Jsoup.parse(htmlString);
        assertEquals(2,utils.countExternalLinks(doc.body(), "//google.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void performCountExternalLinksWithNullInput1() {
        utils.countExternalLinks(null, "//google.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void performCountExternalLinksWithNullInput2() {
        String htmlString = "<!DOCTYPE html><html><body>"
                            + "<a href=\"//google.com\">Google</a>"
                            + "<a href=\"//yahoo.com\">Yahoo</a>"
                            + "<a href=\"//twitter.com\">Twitter</a>"
                            + "</body></html>";
        Document doc = Jsoup.parse(htmlString);
        utils.countExternalLinks(doc.body(), null);
    }

    @Test
    public void performIncludesLoginForm() {
        String htmlString = "<!DOCTYPE html><html><body>"
                            + "<form><input type=\"text\" /><input type=\"password\" /><input type=\"submit\" /></form>"
                            + "</body></html>";
        Document doc = Jsoup.parse(htmlString);
        assertTrue(utils.includesLoginForm(doc.body()));
    }

    @Test
    public void performIncludesLoginFormGithub() throws IOException {
        URL resource = this.getClass().getClassLoader().getResource("github.html");

        File file = new File(resource.getFile());
        Document doc = Jsoup.parse(file, "UTF-8");
        assertTrue(utils.includesLoginForm(doc.body()));
    }

    @Test
    public void performIncludesLoginFormSpiegel() throws IOException {
        URL resource = this.getClass().getClassLoader().getResource("spiegel.html");

        File file = new File(resource.getFile());
        Document doc = Jsoup.parse(file, "UTF-8");
        assertTrue(utils.includesLoginForm(doc.body()));
    }
}

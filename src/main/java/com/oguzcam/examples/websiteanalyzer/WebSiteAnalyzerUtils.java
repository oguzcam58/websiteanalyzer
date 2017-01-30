package com.oguzcam.examples.websiteanalyzer;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides necessary features for WebSite analyzing
 */
@Component
public class WebSiteAnalyzerUtils {

    /**
     * Validates the specified URL. HTTP and HTTPS protocols are allowed, others not.
     *
     * @param url Url address as String.
     * @return True if URL is valid, otherwise false.
     */
    public boolean validator(String url) {
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);

        return urlValidator.isValid(url);
    }

    /**
     * Gets domain address from the baseUri. For null and empty inputs, it throws IllegalArgumentException.
     *
     * @param baseUri Base address for related web page.
     * @return Domain address for related web page. If no domain is found, return null.
     */
    public String getDomain(String baseUri) {
        if (baseUri == null || baseUri.isEmpty()) {
            throw new IllegalArgumentException("Specified base uri cannot be null or empty");
        }

        Pattern compile = Pattern.compile("//[^/]*");
        Matcher matcher = compile.matcher(baseUri);
        if(matcher.find()) {
            return matcher.group();
        }

        // No domain is found from specified uri
        return null;
    }

    /**
     * Uses JSoup Document type to find out the HTML version.
     * If there is no specified HTML version.
     * It return HTML 5 because in HTML 5, there is no need to specify HTML version.
     *
     * @param doc Document for which HTML Version needed.
     * @return HTML Version if it is specified, otherwise HTML 5.
     */
    public String getHTMLVersion(Document doc) {
        if (doc == null) {
            throw new IllegalArgumentException("Specified document cannot be null");
        }
        List<Node> nods = doc.childNodes();
        if (nods.isEmpty()) {
            throw new IllegalArgumentException("Specified document must have child nodes");
        }
        for (Node node : nods) {
            if (node instanceof DocumentType) {
                DocumentType documentType = (DocumentType)node;

                String publicid = documentType.attr("publicid");
                if (publicid != null && !publicid.isEmpty()) {
                    Pattern pattern = Pattern.compile("HTML\\s*[\\d.]*");
                    Matcher matcher = pattern.matcher(publicid);
                    if(matcher.find()) {
                        return matcher.group();
                    }
                }
            }
        }
        // If it is not specified, default is HTML5, because,
        // in HTML5, doctype has no explicit version
        return "HTML 5";
    }

    /**
     * Counts heading elements in the page.
     *
     * @param body Body tag of the related web page.
     * @return Counts for every heading elements. Every heading tag's count referred in -1 index.
     * For example; h1 counts is in [0], h2 is in [1].
     */
    public int[] countHeadings(Element body) {
        if (body == null) {
            throw new IllegalArgumentException("Specified body element cannot be null.");
        }

        int[] headingCount = new int[6];
        for (int i = 1; i <= 6; i++) {
            headingCount[i - 1] = body.getElementsByTag("h" + i).size();
        }

        return headingCount;
    }

    /**
     * Counts all elements with "a href" tag.
     *
     * @param body Body tag of the related web page.
     * @return Count of links
     */
    public int countLinks(Element body) {
        if (body == null) {
            throw new IllegalArgumentException("Specified body element cannot be null.");
        }
        return body.select("a[href]").size();
    }

    /**
     * Counts external links in the page.
     * Subdomains are also considered as external links because it can point to a different host/IP.
     *
     * @param body Body tag of the related web page.
     * @param domain Domain address of the related web page.
     * @return Count of external links
     */
    public int countExternalLinks(Element body, String domain) {
        if (body == null || domain == null) {
            throw new IllegalArgumentException("Specified body element or domain element cannot be null.");
        }
        // Do not include page anchors
        Elements links = body.select("a[href~=^[^#]+]");
        int counter = 0;
        Pattern compile = Pattern.compile("//[^/]*");
        for (Element link : links) {
            Matcher matcher = compile.matcher(link.attr("href"));
            if (matcher.find()) {
                counter += matcher.group().equals(domain) ? 0 : 1;
            }
        }
        return counter;
    }

    /**
     * Traverses over forms and checks for login forms. Returns true if it finds.
     *
     * @param body Body tag of the related web page.
     * @return True if the web page has one form which includes one text input,
     * one password input and one submit button, otherwise False
     */
    public boolean includesLoginForm(Element body) {
        Elements forms = body.select("form");

        // Basic check for login form.
        // Generally a login form has one text input, one password input and one submit button.
        for(Element form : forms) {
            int textInputCount = form.select("input[type=text]").size();
            int passCount = form.select("input[type=password]").size();
            int submitCount = form.select("input[type=submit]").size();
            if (textInputCount == 1 && passCount == 1 && submitCount == 1) {
                return true;
            }
        }

        return false;
    }
}

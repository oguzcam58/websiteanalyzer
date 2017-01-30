package com.oguzcam.examples.websiteanalyzer;

import com.oguzcam.examples.message.BootstrapMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.oguzcam.examples.message.BootstrapMessage.BSMessageType.DANGER;
import static com.oguzcam.examples.message.BootstrapMessage.BSMessageType.SUCCESS;

@Controller
@RequestMapping("/")
public class WebSiteAnalyzerController {

    private static final Logger LOG = LoggerFactory.getLogger(WebSiteAnalyzerController.class.getName());

	@Autowired
	private WebSiteAnalyzerUtils utils;

	@ModelAttribute("webSiteBean")
	public WebSiteBean createFormBean() {
		return new WebSiteBean();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String form() {
		return "home";
	}

    /**
     * Processes submit of the form
     *
     * @param webSiteBean Session attribute
     * @param result BindingResult
     * @param model Model
     * @return Returns page to go
     */
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(WebSiteBean webSiteBean,
							 BindingResult result, Model model) {
        List<BootstrapMessage> messages = new ArrayList<>();
        model.addAttribute("messages", messages);

        // Before process, set false
        webSiteBean.setNewResultExists(false);

		if(result.hasErrors()) {
            messages.add(new BootstrapMessage(DANGER,
                    "Provided form has errors. Please make sure that you have provided correct values."));
			return "home";
		}

        // Check if the URL specified is valid
        if(!utils.validator(webSiteBean.getUrlString())) {
			webSiteBean.setUrlString("http://");
			messages.add(new BootstrapMessage(DANGER, "Specified url is not valid, please specify a valid URL."));
            return "home";
		}

		Document doc = getDocument(webSiteBean.getUrlString(), messages);
        if (doc == null) {
            messages.add(new BootstrapMessage(DANGER, "Couldn't read web page with the specified URL."));
        } else {
			webSiteBean.setTitle(doc.title());
			webSiteBean.setHtmlVersion(utils.getHTMLVersion(doc));
			webSiteBean.setHeadingCount(utils.countHeadings(doc.body()));
			webSiteBean.setLinkCount(utils.countLinks(doc.body()));
			webSiteBean.setExternalLinkCount(utils.countExternalLinks(doc.body(), utils.getDomain(doc.baseUri())));
			webSiteBean.setInternalLinkCount(webSiteBean.getLinkCount() - webSiteBean.getExternalLinkCount());
			webSiteBean.setLoginFormIncluded(utils.includesLoginForm(doc.body()));
            webSiteBean.setNewResultExists(true);
			messages.add(new BootstrapMessage(SUCCESS, "Specified URL has been analyzed successfully."));
		}

		return "home";
	}

    /**
     * Gets document from specified url
     *
     * @param urlString Url to get document from
     * @param messages Messages to write if there is an exception
     * @return Document gathered by using the url, if not successful, returns null
     */
	Document getDocument(String urlString, List<BootstrapMessage> messages) {
		try {
			return Jsoup.connect(urlString).timeout(5000).get();
		} catch (IOException e) {
			messages.add(new BootstrapMessage(DANGER, "Couldn't reach to the URL."));
			LOG.error("IOException while trying to read the URL.", e);
		}
        return null;
	}

}
package com.oguzcam.examples.websiteanalyzer;

import java.util.Arrays;

/**
 * WebSiteBean to keep the url and results
 */
public class WebSiteBean {

    private String urlString = "http://";
    private String htmlVersion;
    private String title;
    private int[] headingCount;
    private int linkCount;
    private int externalLinkCount;
    private int internalLinkCount;
    private boolean loginFormIncluded;
    private boolean newResultExists;

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getHtmlVersion() {
        return htmlVersion;
    }

    public void setHtmlVersion(String htmlVersion) {
        this.htmlVersion = htmlVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getHeadingCount() {
        return headingCount;
    }

    public void setHeadingCount(int[] headingCount) {
        this.headingCount = headingCount;
    }

    public int getLinkCount() {
        return linkCount;
    }

    public void setLinkCount(int linkCount) {
        this.linkCount = linkCount;
    }

    public int getExternalLinkCount() {
        return externalLinkCount;
    }

    public void setExternalLinkCount(int externalLinkCount) {
        this.externalLinkCount = externalLinkCount;
    }

    public int getInternalLinkCount() {
        return internalLinkCount;
    }

    public void setInternalLinkCount(int internalLinkCount) {
        this.internalLinkCount = internalLinkCount;
    }

    public boolean isLoginFormIncluded() {
        return loginFormIncluded;
    }

    public void setLoginFormIncluded(boolean loginFormIncluded) {
        this.loginFormIncluded = loginFormIncluded;
    }

    public boolean isNewResultExists() {
        return newResultExists;
    }

    public void setNewResultExists(boolean newResultExists) {
        this.newResultExists = newResultExists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebSiteBean that = (WebSiteBean) o;

        return urlString != null ? urlString.equals(that.urlString) : that.urlString == null;

    }

    @Override
    public int hashCode() {
        return urlString != null ? urlString.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WebSiteBean{" +
                "urlString='" + urlString + '\'' +
                ", htmlVersion='" + htmlVersion + '\'' +
                ", title='" + title + '\'' +
                ", headingCount=" + Arrays.toString(headingCount) +
                ", linkCount=" + linkCount +
                ", externalLinkCount=" + externalLinkCount +
                ", internalLinkCount=" + internalLinkCount +
                ", loginFormIncluded=" + loginFormIncluded +
                ", newResultExists=" + newResultExists +
                '}';
    }
}

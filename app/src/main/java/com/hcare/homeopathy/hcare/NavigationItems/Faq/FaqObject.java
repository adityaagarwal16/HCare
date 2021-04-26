package com.hcare.homeopathy.hcare.NavigationItems.Faq;

public class FaqObject {

    private final String heading, subHeading;

    public FaqObject(String heading, String subHeading) {
        this.heading = heading;
        this.subHeading = subHeading;
    }

    public String getHeading() {
        return heading;
    }

    public String getSubHeading() {
        return subHeading;
    }

}

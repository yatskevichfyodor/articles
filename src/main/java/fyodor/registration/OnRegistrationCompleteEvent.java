package fyodor.registration;

import fyodor.model.User;

import java.util.Locale;

public class OnRegistrationCompleteEvent {
    private User user;
    private Locale locale;
    private String appUrl;

    public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
        super();
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}

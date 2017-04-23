package com.example.wolf.testseries.ParseModelController;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by WOLF on 13-05-2015.
 */
@ParseClassName("ContactUs")
public class ContactUs extends ParseObject
{
    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public String getPhone() {
        return getString("phone");
    }

    public void setPhone(String phone) {
        put("phone", phone);
    }

    public String getWebsite() {
        return getString("website");
    }

    public void setWebsite(String website) {
        put("website", website);
    }
}

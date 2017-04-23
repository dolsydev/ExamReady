package com.example.wolf.testseries.ParseModelController;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by WOLF on 29-03-2015.
 */
@ParseClassName("CategoriesInfo")

public class CategoriesInfo extends ParseObject {
    // Ensure that your subclass has a public default constructor
    public CategoriesInfo() {
        super();
    }

    // Add a constructor that contains core properties
    public CategoriesInfo(int categoryId, String categoryName) {
        super();
        setCategoryId(categoryId);
        setCategoryName(categoryName);
    }

    // Use getString and others to access fields
    public String getCategoryName() {
        return getString("categoryName");
    }

    // Use put to modify field values
    public void setCategoryName(String value) {
        put("categoryName", value);
    }

    // Get the user for this item
    public int getCategoryId()  {
        return getInt("categoryId");
    }

    // Associate each item with a user
    public void setCategoryId(int categoryId)
    {
        put("categoryId", categoryId);
    }
}

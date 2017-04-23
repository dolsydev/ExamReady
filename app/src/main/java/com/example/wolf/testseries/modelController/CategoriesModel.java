package com.example.wolf.testseries.modelController;

/**
 * Created by WOLF on 29-03-2015.
 */
public class CategoriesModel
{


    public CategoriesModel(int categoryId, String categoryName) {
        setCategoryId(categoryId);
        setCategoryName(categoryName);
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    private int categoryId;
    private String categoryName;

}

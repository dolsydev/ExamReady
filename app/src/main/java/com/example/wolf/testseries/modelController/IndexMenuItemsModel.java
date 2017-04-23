package com.example.wolf.testseries.modelController;

public class IndexMenuItemsModel {
	
	public IndexMenuItemsModel(String menuTitle, int imageId)
	{
		setMenuTitle(menuTitle);
		setMenuImagePosition(imageId);
	}

	private String menuTitle;
	private int menuImagePosition;
	
	public String getMenuTitle() {
		return menuTitle;
	}
	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}
	public int getMenuImagePosition() {
		return menuImagePosition;
	}
	public void setMenuImagePosition(int menuImagePosition) {
		this.menuImagePosition = menuImagePosition;
	}
	
}

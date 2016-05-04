package com.ctrl.forum.entity;

/**
 * ��Ʒ����
 * 
 * @author zkw
 * 
 */
public class FoodTypeModel {
	private String foodTypeName;
	private boolean isChangeColor;
	private int itemPosition;// ��Ʒ�����ڲ�Ʒ�б��ж�Ӧ��λ��
	private int typePosition;// ��¼һ�µ�ǰ��Ʒ�����ڲ�Ʒ�����б��е�position

	public FoodTypeModel(String foodTypeName, boolean isChangeColor, int itemPosition, int typePosition) {
		super();
		this.foodTypeName = foodTypeName;
		this.isChangeColor = isChangeColor;
		this.itemPosition = itemPosition;
		this.typePosition = typePosition;
	}

	public String getFoodTypeName() {
		return foodTypeName;
	}

	public void setFoodTypeName(String foodTypeName) {
		this.foodTypeName = foodTypeName;
	}

	public boolean isChangeColor() {
		return isChangeColor;
	}

	public void setChangeColor(boolean isChangeColor) {
		this.isChangeColor = isChangeColor;
	}

	public int getItemPosition() {
		return itemPosition;
	}

	public void setItemPosition(int itemPosition) {
		this.itemPosition = itemPosition;
	}

	public int getTypePosition() {
		return typePosition;
	}

	public void setTypePosition(int typePosition) {
		this.typePosition = typePosition;
	}

}

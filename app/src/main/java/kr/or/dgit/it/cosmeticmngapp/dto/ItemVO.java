package kr.or.dgit.it.cosmeticmngapp.dto;

public abstract class ItemVO {
    public static final int TYPE_HEADER=0;
    public static final int TYPE_DATA=1;

    public abstract int getType();
}

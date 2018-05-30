package kr.or.dgit.it.cosmeticmngapp;

public class Item {
    String img;
    String name;
    String openDate;
    String endDate;
    String favorite;

    public Item(String img, String name, String openDate, String endDate, String favorite) {
        this.img = img;
        this.name = name;
        this.openDate = openDate;
        this.endDate = endDate;
        this.favorite = favorite;
    }
}

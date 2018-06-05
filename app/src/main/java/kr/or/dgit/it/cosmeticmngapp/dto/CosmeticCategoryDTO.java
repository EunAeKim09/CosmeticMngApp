package kr.or.dgit.it.cosmeticmngapp.dto;

public class CosmeticCategoryDTO {
    private int _id;
    private String name;
    private int durationY;
    private int durationM;
    private int durationD;

    public CosmeticCategoryDTO(int _id, String name, int durationY, int durationM, int durationD) {
        this._id = _id;
        this.name = name;
        this.durationY = durationY;
        this.durationM = durationM;
        this.durationD = durationD;
    }

    public CosmeticCategoryDTO(String name, int durationY, int durationM, int durationD) {
        this.name = name;
        this.durationY = durationY;
        this.durationM = durationM;
        this.durationD = durationD;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationY() {
        return durationY;
    }

    public void setDurationY(int durationY) {
        this.durationY = durationY;
    }

    public int getDurationM() {
        return durationM;
    }

    public void setDurationM(int durationM) {
        this.durationM = durationM;
    }

    public int getDurationD() {
        return durationD;
    }

    public void setDurationD(int durationD) {
        this.durationD = durationD;
    }

    public CosmeticCategoryDTO() {
    }

    @Override
    public String toString() {
        return name;
    }
}

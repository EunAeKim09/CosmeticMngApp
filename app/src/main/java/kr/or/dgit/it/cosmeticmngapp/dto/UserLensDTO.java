package kr.or.dgit.it.cosmeticmngapp.dto;

public class UserLensDTO {
    private int _id;
    private String name;
    private String img;
    private String openDate;
    private String endDate;
    private String memo;
    private String favorite;
    private String cate_id;

    public UserLensDTO(int _id, String name, String img, String openDate, String endDate, String memo, String favorite, String cate_id) {
        this._id = _id;
        this.name = name;
        this.img = img;
        this.openDate = openDate;
        this.endDate = endDate;
        this.memo = memo;
        this.favorite = favorite;
        this.cate_id = cate_id;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }
}

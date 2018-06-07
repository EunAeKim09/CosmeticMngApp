package kr.or.dgit.it.cosmeticmngapp.dto;

public class UserLens extends ItemVO{
    private int _id;
    private String name;
    private String img;
    private String openDate;
    private String endDate;
    private String memo;
    private int favorite;
    private int cate_id;

    public UserLens() {
    }

    public UserLens(int _id, String name, String img, String openDate, String endDate, String memo, int favorite, int cate_id) {
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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    @Override
    public int getType() {
        return ItemVO.TYPE_DATA;
    }
}

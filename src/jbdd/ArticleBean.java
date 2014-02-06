package jbdd;

import java.io.Serializable;
import java.math.BigDecimal;

public class ArticleBean implements Serializable {

    private int _id;
    private String _name;
    private int _availability;
    private BigDecimal _price;
    private int _idCategory;

    public ArticleBean() {}
    public ArticleBean(int id, String name, int availability, BigDecimal price, int idCategory) {
        _id = id;
        _name = name;
        _availability = availability;
        _price = price;
        _idCategory = idCategory;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public String get_name() {
        return _name;
    }
    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_idCategory() {
        return _idCategory;
    }

    public void set_idCategory(int _idCategory) {
        this._idCategory = _idCategory;
    }

    public int get_availability() {
        return _availability;
    }

    public void set_availability(int _availability) {
        this._availability = _availability;
    }

    public BigDecimal get_price() {
        return _price;
    }

    public void set_price(BigDecimal _price) {
        this._price = _price;
    }

}

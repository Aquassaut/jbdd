package jbdd;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aquassaut
 * Date: 1/25/14
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderBean implements Serializable {
    private int _id;
    private Calendar _date;
    private BigDecimal _price;
    private List<ArticleBean> _articles;

    public OrderBean() {}
    public OrderBean(int id, Calendar date, BigDecimal price, List<ArticleBean> articles) {
        _id = id;
        _date = date;
        _price = price;
        _articles = articles;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Calendar get_date() {
        return _date;
    }

    public void set_date(Calendar _date) {
        this._date = _date;
    }

    public BigDecimal get_price() {
        return _price;
    }

    public void set_price(BigDecimal _price) {
        this._price = _price;
    }

    public List<ArticleBean> get_articles() {
        return _articles;
    }

    public void set_articles(List<ArticleBean> _articles) {
        this._articles = _articles;
    }
}

package jbdd;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aquassaut
 * Date: 1/25/14
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaleBean implements Serializable {
    private int _id;
    private Date _date;
    private BigDecimal _price;
    private int _client;
    private List<ArticleBean> _articles;

    public SaleBean() {}
    public SaleBean(int id, Date date, BigDecimal price, int client, List<ArticleBean> articles) {
        _id = id;
        _date = date;
        _price = price;
        _client = client;
        _articles = articles;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
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

    public int get_client() {
        return _client;
    }

    public void set_client(int _client) {
        this._client = _client;
    }

}

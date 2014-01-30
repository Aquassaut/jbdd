package jbdd;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aquassaut
 * Date: 1/25/14
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasketBean implements Serializable {
    private int _id;
    private ClientBean _user;
    private List<ArticleBean> _articles;

    public BasketBean() {}
    public BasketBean(int id, List<ArticleBean> articles, ClientBean user) {
        _id = id;
        _articles = articles;
        _user = user;
    }

    public List<ArticleBean> get_articles() {
        return _articles;
    }

    public void set_articles(List<ArticleBean> _articles) {
        this._articles = _articles;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public ClientBean get_user() {
        return _user;
    }

    public void set_user(ClientBean _user) {
        this._user = _user;
    }
}

package jbdd;

import java.io.Serializable;
import java.util.List;

public class BasketBean implements Serializable {
    private int _id;
    private int _client;
    private List<ArticleBean> _articles;

    public BasketBean() {}
    public BasketBean(int id, List<ArticleBean> articles, int user) {
        _id = id;
        _articles = articles;
        _client = user;
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

    public int get_client() {
        return _client;
    }

    public void set_client(int _client) {
        this._client = _client;
    }


}

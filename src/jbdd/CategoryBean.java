package jbdd;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: aquassaut
 * Date: 1/25/14
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryBean implements Serializable {
    private int _id;
    private String _name;

    public CategoryBean() {}
    public CategoryBean(int id, String name) {
        _id = id;
        _name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
}

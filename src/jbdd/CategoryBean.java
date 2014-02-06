package jbdd;

import java.io.Serializable;

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

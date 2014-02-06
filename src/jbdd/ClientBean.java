package jbdd;

import java.io.Serializable;

public class ClientBean implements Serializable {

    private int _id;
    private String _name;
    private String _password;

    public ClientBean() {}
    public ClientBean(int id, String name, String password) {
        _id = id;
        _name = name;
        _password = password;
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

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }
}

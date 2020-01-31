package ibzssoft.com.modelo.drawerMenu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrawerItemCategory {

    private int id;
    private int numgrupo;
    private String precios;
    private String name;
    private List<DrawerItemCategory> children;
    private String type;

    public DrawerItemCategory() {
    }

    public DrawerItemCategory(int id, int numgrupo, String name) {
        this.id = id;
        this.numgrupo=numgrupo;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getNumgrupo() {
        return numgrupo;
    }

    public void setNumgrupo(int numgrupo) {
        this.numgrupo = numgrupo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DrawerItemCategory> getChildren() {
        return children;
    }

    public void setChildren(List<DrawerItemCategory> children) {
        this.children = children;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrecios() {
        return precios;
    }

    public void setPrecios(String precios) {
        this.precios = precios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrawerItemCategory that = (DrawerItemCategory) o;

        if (id != that.id) return false;
        if (numgrupo != that.numgrupo) return false;
        if (precios != null ? !precios.equals(that.precios) : that.precios != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (children != null ? !children.equals(that.children) : that.children != null)
            return false;
        return type != null ? type.equals(that.type) : that.type == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + numgrupo;
        result = 31 * result + (precios != null ? precios.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DrawerItemCategory{" +
                "id=" + id +
                ", numgrupo=" + numgrupo +
                ", precios='" + precios + '\'' +
                ", name='" + name + '\'' +
                ", children=" + children +
                ", type='" + type + '\'' +
                '}';
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
}

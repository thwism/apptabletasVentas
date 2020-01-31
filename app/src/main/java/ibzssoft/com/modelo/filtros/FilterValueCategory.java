package ibzssoft.com.modelo.filtros;

public class FilterValueCategory {

    private long id = 0;
    private String value;
    private String code;

    public FilterValueCategory() {
    }

    public FilterValueCategory(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FilterValuecategory{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}

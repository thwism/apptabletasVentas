package ibzssoft.com.modelo.filtros;

import java.util.List;

public class FilterTypeCategory extends FilterType {

    /**
     * Currently selected value
     */
    private transient FilterValueCategory selectedValue = null;

    private List<FilterValueCategory> values;

    public FilterTypeCategory() {
    }

    public FilterValueCategory getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(FilterValueCategory selectedValue) {
        this.selectedValue = selectedValue;
    }

    public List<FilterValueCategory> getValues() {
        return values;
    }

    public void setValues(List<FilterValueCategory> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FilterTypeCategory that = (FilterTypeCategory) o;

        if (selectedValue != null ? !selectedValue.equals(that.selectedValue) : that.selectedValue != null)
            return false;
        return !(values != null ? !values.equals(that.values) : that.values != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (selectedValue != null ? selectedValue.hashCode() : 0);
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + ". FilterTypeCategory{" +
                "selectedValue=" + selectedValue +
                ", values= ..." +
                '}';
    }
}

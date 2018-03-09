/**
 * Represents an attribute of a Dataset
 * @param <T> the type of attribute (int, double or String)
 */
public class Attribute<T> {
    private T value;
    private AttributeTypes type;

    /**
     * Creates a new instance of an attribute
     * @param value the value of the attribute
     * @param type the type of the attribute
     */
    public Attribute(T value, AttributeTypes type){
        this.value = value;
        this.type = type;
    }

    /**
     * Return the attribute value
     * @return the attribute value
     */
    public T getValue() {
        return value;
    }

    /**
     * Returns the attribute type
     * @return an enum-instance of AttributeTypes representing the attribute type
     */
    public AttributeTypes getType(){
        return type;
    }
}

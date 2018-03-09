import java.util.ArrayList;

/**
 * Represents a single row of data
 */
public class Dataset {
    private ArrayList<Attribute> attributes = new ArrayList<>();

    /**
     * Creates a new instance of a Dataset
     * @param attributes a String-Array containing all attribute values for this Dataset
     */
    public Dataset(String[] attributes){
        for(String s : attributes){
            addAttribute(s);
        }
    }

    /**
     * Adds an attribute to the attribute list
     * Checks if the given attribute is an Integer, Double or String and adds it to the list
     * @param attr the attribute value as String
     */
    private void addAttribute(String attr){
        // Remove leading and trailing whitespaces
        attr = attr.trim();

        // Check if attribute is int
        try{
            int i = Integer.parseInt(attr);
            attributes.add(new Attribute(i, AttributeTypes.INTEGER));
            return;
        }catch(Exception e){}

        // Check if attribute is double
        try{
            double d = Double.parseDouble(attr);
            attributes.add(new Attribute(d, AttributeTypes.DECIMAL));
            return;
        }catch(Exception e){}

        // Attribute is text
        attributes.add(new Attribute(attr, AttributeTypes.TEXT));
    }

    /**
     * Returns a single attribute from the attribute list
     * @param index an int representing the index of the desired attribute
     * @return an Attribute instance containing the desired attribute
     */
    public Attribute getAttribute(int index){
        return attributes.get(index);
    }

    /**
     * Returns the number of attributes in the attribute list
     * @return an int containing the number of attributes
     */
    public int getAttributeCount(){
        return attributes.size();
    }
}

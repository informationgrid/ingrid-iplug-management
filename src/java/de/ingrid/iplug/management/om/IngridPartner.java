package de.ingrid.iplug.management.om;

public class IngridPartner {

    private Long id;
    private String ident;
    private String name;
    private int sortkey;
    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return Returns the ident.
     */
    public String getIdent() {
        return ident;
    }
    /**
     * @param ident The ident to set.
     */
    public void setIdent(String ident) {
        this.ident = ident;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the sortkey.
     */
    public int getSortkey() {
        return sortkey;
    }
    /**
     * @param sortkey The sortkey to set.
     */
    public void setSortkey(int sortkey) {
        this.sortkey = sortkey;
    }
    
}
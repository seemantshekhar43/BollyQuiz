package comandydevo.wixsite.seemantshekhar43.bollyquiz;

/**
 * Created by A_Devy on 11/29/2017.
 */

public class Category_model {


    private String name;
    private String image;
    private String level;

    public Category_model(){

    }
    public Category_model(String name, String image, String level) {
        this.name = name;
        this.image = image;
        this.level = level;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


}

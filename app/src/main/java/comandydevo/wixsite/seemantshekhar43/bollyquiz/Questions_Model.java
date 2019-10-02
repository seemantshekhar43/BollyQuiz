package comandydevo.wixsite.seemantshekhar43.bollyquiz;

/**
 * Created by A_Devy on 12/1/2017.
 */

public class Questions_Model {


    private String text, answer_a, answer_b, answer_c, answer_d, correct_answer, image_url, category_id, contains_image;

    public Questions_Model(){
    }
    public Questions_Model(String text, String answer_a, String answer_b, String answer_c, String answer_d, String correct_answer, String contains_image, String image_url, String category_id) {
        this.text = text;
        this.answer_a = answer_a;
        this.answer_b = answer_b;
        this.answer_c = answer_c;
        this.answer_d = answer_d;
        this.correct_answer = correct_answer;
        this.contains_image = contains_image;
        this.image_url = image_url;
        this.category_id = category_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer_a() {
        return answer_a;
    }

    public void setAnswer_a(String answer_a) {
        this.answer_a = answer_a;
    }

    public String getAnswer_b() {
        return answer_b;
    }

    public void setAnswer_b(String answer_b) {
        this.answer_b = answer_b;
    }

    public String getAnswer_c() {
        return answer_c;
    }

    public void setAnswer_c(String answer_c) {
        this.answer_c = answer_c;
    }

    public String getAnswer_d() {
        return answer_d;
    }

    public void setAnswer_d(String answer_d) {
        this.answer_d = answer_d;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }



    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getContains_image() {
        return contains_image;
    }

    public void setContains_image(String contains_image) {
        this.contains_image = contains_image;
    }
}

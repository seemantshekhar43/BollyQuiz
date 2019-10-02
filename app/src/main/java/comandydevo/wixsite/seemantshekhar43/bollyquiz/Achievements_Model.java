package comandydevo.wixsite.seemantshekhar43.bollyquiz;

/**
 * Created by A_Devy on 12/4/2017.
 */

public class Achievements_Model {

    public int star_quizzes;
    public int struggler_quizzes;
    public int superstar_quizzes;
    public int total_quizzes;
    public int star_points;
    public int struggler_points;
    public int superstar_points;
    public int total_points;
    public int xp;
    public String most_played;
    public String masters_in;

    public Achievements_Model(){

    }

    public Achievements_Model(int star_quizzes, int struggler_quizzes, int superstar_quizzes, int total_quizzes, int star_points, int struggler_points, int superstar_points, int total_points, int xp, String most_played, String masters_in) {
        this.star_quizzes = star_quizzes;
        this.struggler_quizzes = struggler_quizzes;
        this.superstar_quizzes = superstar_quizzes;
        this.total_quizzes = total_quizzes;
        this.star_points = star_points;
        this.struggler_points = struggler_points;
        this.superstar_points = superstar_points;
        this.total_points = total_points;
        this.xp = xp;
        this.most_played = most_played;
        this.masters_in = masters_in;
    }

    public int getStar_quizzes() {
        return star_quizzes;
    }

    public void setStar_quizzes(int star_quizzes) {
        this.star_quizzes = star_quizzes;
    }

    public int getStruggler_quizzes() {
        return struggler_quizzes;
    }

    public void setStruggler_quizzes(int struggler_quizzes) {
        this.struggler_quizzes = struggler_quizzes;
    }

    public int getSuperstar_quizzes() {
        return superstar_quizzes;
    }

    public void setSuperstar_quizzes(int superstar_quizzes) {
        this.superstar_quizzes = superstar_quizzes;
    }

    public int getTotal_quizzes() {
        return total_quizzes;
    }

    public void setTotal_quizzes(int total_quizzes) {
        this.total_quizzes = total_quizzes;
    }

    public int getStar_points() {
        return star_points;
    }

    public void setStar_points(int star_points) {
        this.star_points = star_points;
    }

    public int getStruggler_points() {
        return struggler_points;
    }

    public void setStruggler_points(int struggler_points) {
        this.struggler_points = struggler_points;
    }

    public int getSuperstar_points() {
        return superstar_points;
    }

    public void setSuperstar_points(int superstar_points) {
        this.superstar_points = superstar_points;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getMost_played() {
        return most_played;
    }

    public void setMost_played(String most_played) {
        this.most_played = most_played;
    }

    public String getMasters_in() {
        return masters_in;
    }

    public void setMasters_in(String masters_in) {
        this.masters_in = masters_in;
    }
}

package comandydevo.wixsite.seemantshekhar43.bollyquiz;

/**
 * Created by A_Devy on 11/28/2017.
 */

public class Profile_model {
   private String city;
    private String country;
    private String mobile;
    private String user_name;
    private String profile_image;
    private String thumb_profile_image;
    private String date_joined;


    public Profile_model(){

    }

    public Profile_model(String city, String country, String mobile, String user_name, String profile_image,String thumb_profile_image, String date_joined) {
        this.city = city;
        this.country = country;
        this.mobile = mobile;
        this.user_name = user_name;
        this.profile_image = profile_image;
        this.thumb_profile_image = thumb_profile_image;
        this.date_joined = date_joined;

    }
    public String getThumb_profile_image() {
        return thumb_profile_image;
    }

    public void setThumb_profile_image(String thumb_profile_image) {
        this.thumb_profile_image = thumb_profile_image;
    }


    public String getProfile_image(){
        return profile_image;
    }

    public void setProfile_image(String profile_image){
        this.profile_image = profile_image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }


}

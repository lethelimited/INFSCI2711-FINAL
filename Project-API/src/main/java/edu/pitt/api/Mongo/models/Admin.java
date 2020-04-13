package edu.pitt.api.Mongo.models;

//import java.util.Arrays;
//import java.util.List;
//
//import org.bson.types.ObjectId;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;

//@Document("user")
public class Admin {
//
//    @Id
//    private ObjectId _id;
//    @Field
//    private boolean isAdmin;
//    @Field
//    private boolean isAnonymous=false;
//    @Field
//    private String usrname;
//    @Field
//    private String pwd;
//    @Field
//    private String email;
//    @Field
//    private String city;
//    @Field
//    private String state;
//    @Field
//    private String phone;
//    @Field
//    private String[] report;
//
//
//
//    //Constructors
//    public Admin(){}
//    public Admin(ObjectId _id, String usrname, boolean isAdmin, String pwd, String city, String state, String email, String phone, String[] report)
//    {
//        this._id = _id;
//        this.usrname = usrname;
//        this.isAdmin = isAdmin;
//        this.pwd = pwd;
//        this.city = city;
//        this.state = state;
//        this.email = email;
//        this.phone = phone;
//        this.report= report;
//    }
//
//    //ObjectId needs to be converted to String
//    public String get_id() { return _id.toHexString(); }
//    public void set_id(ObjectId _id) { this._id = _id; }
//
//    public String getUsrname() { return usrname; }
//    public void setUsrname(String usrname) { this.usrname = usrname; }
//
//    public boolean isAdmin() { return isAdmin; }
//    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
//
//    public String getPwd() { return pwd; }
//    public void setPwd(String pwd) { this.pwd = pwd; }
//
//    public String getCity() { return city; }
//    public void setCity(String city) { this.city = city; }
//
//    public String getState() { return state; }
//    public void setState(String state) { this.state = state; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//    public String getPhone() { return phone; }
//    public void setPhone(String phone) { this.phone = phone; }
//
//    public String[] getReport() { return report; }
//    public void setReport(String[] report) { this.report = report; }
//
//    public boolean isAnonymous(){return isAnonymous;}
//    public void setAnonymous(boolean isAnonymous) {
//        this.isAnonymous = isAnonymous;
//    }
//
//    public List<Role> getRoles() {
//        if (isAdmin) {
//            return Arrays.asList(Role.ROLE_ADMIN);
//        } else {
//            return Arrays.asList(Role.ROLE_CLIENT);
//        }
//    }

}
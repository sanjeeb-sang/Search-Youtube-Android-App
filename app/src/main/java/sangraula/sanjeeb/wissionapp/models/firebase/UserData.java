package sangraula.sanjeeb.wissionapp.models.firebase;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserData {

    public String uid;

    public String gender;

    public String age;

    public UserData() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    public UserData(String uid, String gender, String age) {

        this.uid = uid;

        this.gender = gender;

        this.age = age;

    }


    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("uid", uid);

        result.put("gender", gender);

        result.put("age", age);

        return result;
    }


}


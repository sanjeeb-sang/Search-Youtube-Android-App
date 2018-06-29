package sangraula.sanjeeb.wissionapp.data;

import com.google.firebase.auth.FirebaseUser;

public class FirebasePaths {

    public static class UserData {

        public static final String BASE_PATH = "user-data/";

        public static final String getUsersPath (FirebaseUser user) {

            return BASE_PATH + user.getUid() + "/";
        }

    }

}

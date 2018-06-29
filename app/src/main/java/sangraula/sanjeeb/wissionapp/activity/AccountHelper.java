package sangraula.sanjeeb.wissionapp.activity;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.widget.Toast.makeText;

/**
 * Created by Sanjeeb on 8/26/2017.
 */


public class AccountHelper {

    public static void verifyEmail(final Context context) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            makeText(context, "Verifying Email " + user.getEmail(), Toast.LENGTH_SHORT).show();

            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                makeText(context, "Verification Email Sent!!", Toast.LENGTH_SHORT).show();

                            } else {
                                makeText(context, "Couldn't send verification Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            makeText(context, "You're not logged in.", Toast.LENGTH_SHORT).show();
        }
    }


    public static void updateProfilePic(final Context context, final Uri imageUri, final @Nullable Callback callback) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String refString = "users/" + user.getUid() + "/image/";

        if (user != null) {

            StorageReference ref = FirebaseStorage.getInstance().getReference(refString);

            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    FirebaseStorage.getInstance().getReference(refString)
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(uri)
                                            .build();


                                    user.updateProfile(profileUpdates)

                                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        makeText(context, "Profile Picture is changed", Toast.LENGTH_SHORT).show();

                                                        if (callback != null) {
                                                            callback.onSuccess(task);
                                                        }

                                                    } else {

                                                        makeText(context,
                                                                "Update Failed. " + task.getException().getLocalizedMessage(),
                                                                Toast.LENGTH_LONG).show();

                                                        if (callback != null) {
                                                            callback.onFailure(task);
                                                        }

                                                    }
                                                }
                                            })
                                    ;
                                }
                            });
                }
            });

        }

    }


    public static void updateUsername(final Context context, String username, final @Nullable Callback callback) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        if (user != null) {

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                makeText(context, "User Name updated.", Toast.LENGTH_SHORT).show();

                                if (callback != null) {
                                    callback.onSuccess(task);
                                }

                            } else {

                                makeText(context,
                                        "Update Failed. " + task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();

                                if (callback != null) {
                                    callback.onFailure(task);
                                }

                            }
                        }
                    });

        }


    }


    public interface Callback {

        void onSuccess(Task<Void> task);

        void onFailure(Task<Void> task);
    }

}

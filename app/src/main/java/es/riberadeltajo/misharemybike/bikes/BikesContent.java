package es.riberadeltajo.misharemybike.bikes;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.riberadeltajo.misharemybike.MyItemRecyclerViewAdapter;
import es.riberadeltajo.misharemybike.pojos.Bike;
import es.riberadeltajo.misharemybike.ui.home.HomeFragment;

public class BikesContent {
    //List of all the bikes to be listed in the RecyclerView
    public static List<Bike> ITEMS = new ArrayList<>();
    public static String selectedDate;
    public static DatabaseReference mDatabase;
    public static StorageReference mStorageReference;
    public static MyItemRecyclerViewAdapter adapter;



    public static void loadBikesList() {
            adapter=new MyItemRecyclerViewAdapter(ITEMS);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Log.d("loading bikes from firebase", "da3 "+mDatabase.toString());

        mDatabase.child("bike_list").get().addOnCompleteListener(
                new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });


            mDatabase.child("bikes_list").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("Hola3", "da3 "+mDatabase.toString());

                    ITEMS.clear();
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Log.d("Hola2", "da3 "+mDatabase.toString());
                        Bike bike = productSnapshot.getValue(Bike.class);
                        downloadPhoto(bike);
                        ITEMS.add(bike);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

    }

    private static void downloadPhoto(Bike c) {
        mStorageReference= FirebaseStorage.getInstance().getReferenceFromUrl(c.getImage());
        try {
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            final File localFile = File.createTempFile("PNG_" + timeStamp, ".png");
            mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Insert the downloaded image in its right position at the ArrayList

                    String url = "gs://" + taskSnapshot.getStorage().getBucket() + "/" + taskSnapshot.getStorage().getName();
                    Log.d("Fotos", "Loaded " + url);
                    for (Bike c : ITEMS) {
                        if (c.getImage().equals(url)) {
                            c.setPhoto(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                            adapter.notifyDataSetChanged();
                            Log.d("Fotos", "Loaded pic " + c.getImage() + ";" + url + localFile.getAbsolutePath());
                        }
                    }
                }

            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /*public static void loadBikesFromJSON(Context c) {

        String json = null;
        try {
            InputStream is =
                    c.getAssets().open("bikeList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray couchList = jsonObject.getJSONArray("bike_list");
            for (int i = 0; i < couchList.length(); i++) {
                JSONObject jsonCouch = couchList.getJSONObject(i);
                String owner = jsonCouch.getString("owner");
                String description = jsonCouch.getString("description");
                String city=jsonCouch.getString("city");
                String location=jsonCouch.getString("location");
                String email=jsonCouch.getString("email");
                Bitmap photo=null;
                try {
                    photo= BitmapFactory.decodeStream(
                            c.getAssets().open("images/"+
                                    jsonCouch.getString("image")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ITEMS.add(new BikesContent.Bike(photo,owner,description,city,location,email));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }*/

    /*public static class Bike {
        private Bitmap photo;
        private String owner;
        private String description;
        private String city;
        private String location;
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Bitmap getPhoto() {
            return photo;
        }

        public void setPhoto(Bitmap photo) {
            this.photo = photo;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }



        public Bike(Bitmap photo, String owner, String description, String city, String location, String email) {
            this.photo = photo;
            this.owner = owner;
            this.description = description;
            this.city = city;
            this.location = location;
            this.email= email;
        }

        @Override
        public String toString() {
            return owner+" "+description;
        }
    }*/
}

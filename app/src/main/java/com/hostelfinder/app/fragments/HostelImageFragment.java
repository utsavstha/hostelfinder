package com.hostelfinder.app.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hostelfinder.app.BetterActivityResult;
import com.hostelfinder.app.BuildConfig;
import com.hostelfinder.app.HostelInformationActivity;
import com.hostelfinder.app.ImageUtils;
import com.hostelfinder.app.R;
import com.hostelfinder.app.adapter.ImageAdapter;
import com.hostelfinder.app.adapter.TabsAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostelImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostelImageFragment extends Fragment implements ImageAdapter.ImageClick {

    private RecyclerView rvImages;
    StorageReference storageRef;
    String currentPhotoPath;
    ImageAdapter adapter;
    List<String> images;
    private FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseUser user;

    //    Map<String, Object> data = ((HostelInformationActivity) getActivity()).getData();
    public static HostelImageFragment newInstance() {
        HostelImageFragment fragment = new HostelImageFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hostel_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rvImages = view.findViewById(R.id.rvImages);
        images = new ArrayList<>();

        rvImages.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ImageAdapter(images, this);
        rvImages.setAdapter(adapter);

        fetchData();
        view.findViewById(R.id.fab).setOnClickListener(view1 -> {
            mGetContent.launch("image/*");

        });

    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the returned Uri
                try {
                    uploadImage(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });


    private void uploadImage(Uri uri) throws IOException {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;


        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        storageRef = storage.getReference()
                .child(user.getUid() + "images" + timeStamp);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {

            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    updateImage(downloadUri);
                } else {
                    // Handle failures
                    // ...
                }
            });
        });
    }

    private void updateImage(Uri uri) {
        String documentId = ((HostelInformationActivity) getActivity()).getDocumentId();

        DocumentReference newProduct = db.collection("hostel").document(documentId);
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("image", FieldValue.arrayUnion(uri.toString()));
        newProduct.update(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                    fetchData();
                }
            }
        });
    }

    private void fetchData() {
        CollectionReference hostelRef = db.collection("hostel");
        hostelRef.whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("tag", document.getId() + " => " + document.getData());

                                if (document.getData().containsKey("image")){
                                    images.clear();

                                    List<String> image = (List<String>) document.getData().get("image");
                                    if (image != null){
                                        images.addAll(image);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } else {
                            Log.d("tag", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onDelete(int position) {
        String documentId = ((HostelInformationActivity) getActivity()).getDocumentId();
        images.remove(position);

        DocumentReference newProduct = db.collection("hostel").document(documentId);
        Map<String, Object> postMap = new HashMap<>();
//        for(String uri: images){
        postMap.put("image", images);
//        }
        newProduct.update(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                    fetchData();
                }
            }
        });
    }
}
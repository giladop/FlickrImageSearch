package com.imagesearch.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.imagesearch.searchresults.model.data.ImageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;



/**
 * Created by giladopher on 10/05/2017.
 */
public class LikesRepository implements LikesRepositoryContract{


	private static final String TAG = "LikesRepository";


	private FirebaseAuth firebaseAuth;


	private DatabaseReference db;


	@Inject
	public LikesRepository(){
		firebaseAuth = FirebaseAuth.getInstance();
		db = FirebaseDatabase.getInstance().getReference();
	}


	@Override
	public LiveData<List<ImageData>> getUserLike(@NonNull final String userId){

		final MutableLiveData<List<ImageData>> data = new MutableLiveData<>();
		db.addValueEventListener( new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {

				DataSnapshot users = dataSnapshot.child("users");
				DataSnapshot allImages = dataSnapshot.child("all-images");

				DataSnapshot user = users != null ? users.child(firebaseAuth.getCurrentUser().getUid()) : null;
				if (user == null)
					return;

				Map<String, Boolean> imagesLikesByUser = (Map<String, Boolean>)user.child("images").getValue();
				List<ImageData> imageDataList = new ArrayList<>();

				for (Map.Entry<String, Boolean> entry : imagesLikesByUser.entrySet()){
					if (entry.getValue()){
						ImageData imageData = allImages.child(entry.getKey()).getValue(ImageData.class);

						String farm = (String)allImages.child(entry.getKey()).child("farm").getValue();
						String server = (String)allImages.child(entry.getKey()).child("server").getValue();
						String secret = (String)allImages.child(entry.getKey()).child("secret").getValue();
						imageData.setServer(server);
						imageData.setSecret(secret);
						imageData.setFarm(farm);

						imageDataList.add(imageData);
					}
				}
				data.setValue(imageDataList);

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				// Getting Post failed, log a message
				Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
				// ...


			}
		});


		return data;
	}


	@Override
	public void getAllLikes(@NonNull final LikesCallBack callBack){

		db.addListenerForSingleValueEvent( new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot){

				DataSnapshot allImages = dataSnapshot.child("all-images");
			}


			@Override
			public void onCancelled(DatabaseError databaseError){

			}
		});
	}


	@Override
	public void like(@NonNull String userId, @NonNull ImageData data){

	}


	@Override
	public void unLike(@NonNull String userId, @NonNull String imageDataId){

	}

}

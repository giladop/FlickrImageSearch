package com.imagesearch.searchresults.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.imagesearch.FlickerImageSearchApplication;
import com.imagesearch.R;
import com.imagesearch.di.DaggerFlickerImagesComponent;
import com.imagesearch.di.FlickerImagesComponent;
import com.imagesearch.di.FlickerImagesModule;
import com.imagesearch.searchresults.model.data.ImageData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * This a simple activity showing a full screen image.
 * The {@link ImageData} is received via {@link Intent}.
 *
 * @author Gilad Opher
 */
public class FullScreenImageActivity extends AppCompatActivity{


	private static final String IMAGE_DATA_EXTRA = "image_data_extra";



	private static final String CATEGORY_EXTRA = "category_extra";



	@Inject
	Picasso picasso;


	@BindView(R.id.toolbar)
	Toolbar toolbar;


	@BindView(R.id.image_view)
	ImageView imageView;


	@BindView(R.id.favorite)
	ImageView favorite;


	private DatabaseReference db;


	private FirebaseAuth firebaseAuth;


	private boolean isLike = false;
	private boolean isNewUser = false;
	private boolean isNewImage = false;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_screen_image_layout);
		ButterKnife.bind(this);
		inject();

		ImageData imageData = getIntent().getParcelableExtra(IMAGE_DATA_EXTRA);
		firebaseAuth = FirebaseAuth.getInstance();
		db = FirebaseDatabase.getInstance().getReference();

		readCurrentDbState(imageData);


	}



	/**
	 * Setup the fullscreen image and title.
	 */
	private void setupUi(@NonNull final ImageData imageData, boolean showFav){
		picasso.load(imageData.getImageUrl()).into(imageView);
		toolbar.setTitle(imageData.getTitle());

		if (showFav){
			favorite.setVisibility(View.VISIBLE);
			favorite.setImageResource(isLike ? R.drawable.ic_favorite_red_48dp : R.drawable.ic_favorite_gray_48dp);
			favorite.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					onFavoriteClicked(imageData);
				}
			});
		}else
			favorite.setVisibility(View.GONE);
	}



	private void readCurrentDbState(@NonNull final ImageData imageData){

		if (firebaseAuth.getCurrentUser() != null){

			db.addListenerForSingleValueEvent(new ValueEventListener(){
				@Override
				public void onDataChange(DataSnapshot dataSnapshot){
					DataSnapshot users = dataSnapshot.child("users");
					DataSnapshot user = users != null ? users.child(firebaseAuth.getCurrentUser().getUid()) : null;
					isNewUser = user == null;

					DataSnapshot savedImage = !isNewUser ? user.child("images").child(imageData.getId()) : null;
					isLike = savedImage != null && savedImage.getValue() != null && (boolean)savedImage.getValue();

					DataSnapshot images = dataSnapshot.child("all-images");
					isNewImage = images == null || images.child(imageData.getId()).getValue() == null;

					setupUi(imageData, true);
				}

				@Override
				public void onCancelled(DatabaseError databaseError){
					Log.d("DB", "Error: " + databaseError.getDetails());
					setupUi(imageData, false);
				}
			});


//			return user.child("images").child(imageId) != null;

		}else
			setupUi(imageData, false);
	}



	private void onFavoriteClicked(@NonNull ImageData imageData){
		if (firebaseAuth.getCurrentUser() == null){
			//TODO: handle sign in here...
		}else{
			isLike = !isLike;
			favorite.setImageResource(isLike ? R.drawable.ic_favorite_red_48dp : R.drawable.ic_favorite_gray_48dp);
			updateFavoritesDB(firebaseAuth.getCurrentUser(), imageData);
		}
	}



	private void updateFavoritesDB(@NonNull FirebaseUser currentUser, @NonNull ImageData imageData){

		String uid = currentUser.getUid();
		if (isLike)
			imageData.like(uid);
		else
			imageData.unLike(uid);

		final String imageId = imageData.getId();
		//String category = getIntent().getStringExtra(CATEGORY_EXTRA);

		DatabaseReference user = getCurrentUserDbRef(uid);
		DatabaseReference image = getCurrentImageDbRef(imageId);


		Map<String, Object> childUpdates = new HashMap<>();

		Map<String, Object> imageValues = imageData.toMap();


		if (isNewImage){
			childUpdates.put("/users/" + uid + "/images/" + imageId, true);
			childUpdates.put("/all-images/" + imageId, imageValues);
			db.updateChildren(childUpdates);
		}else{
			childUpdates.put("/users/" + uid + "/images/" + imageId, isLike);
			db.updateChildren(childUpdates);

			// TODO: transaction

			image.runTransaction(new Transaction.Handler() {
					@Override
					public Transaction.Result doTransaction(MutableData mutableData) {
						ImageData theImageData = mutableData.getValue(ImageData.class);
						if (theImageData == null) {
							return Transaction.success(mutableData);
						}

						if (theImageData.getLikes().containsKey(uid)) {
							// Unstar the post and remove self from stars
							theImageData.unLike(uid);
						} else {
							theImageData.like(uid);
						}

						// Set value and report transaction success
						mutableData.setValue(theImageData);
						return Transaction.success(mutableData);
					}

					@Override
					public void onComplete(DatabaseError databaseError, boolean b,
							DataSnapshot dataSnapshot) {
						// Transaction completed
						Log.d("DB", "postTransaction:onComplete:" + databaseError);
					}
				});
			}




	}

	private DatabaseReference getCurrentImageDbRef(String imageId){
		if (db.child("all-images") == null) return null;

		return db.child("all-images").child(imageId);
	}

	private DatabaseReference getCurrentUserDbRef(String uid){
		if (db.child("users") == null) return null;

		return db.child("users").child(uid);
	}


	private void inject(){
		FlickerImagesComponent flickerImagesComponent =
			DaggerFlickerImagesComponent.builder()
				.appComponent(FlickerImageSearchApplication.getFlickerImageSearchApplication().getAppComponent())
				.flickerImagesModule(new FlickerImagesModule())
				.build();
		flickerImagesComponent.inject(this);
	}


	/**
	 * Create starting {@link Intent}
	 */
	public static Intent newIntent(@NonNull Context context, @NonNull ImageData imageData, @NonNull String category){
		Intent intent = new Intent(context, FullScreenImageActivity.class);
		intent.putExtra(IMAGE_DATA_EXTRA, imageData);
		intent.putExtra(CATEGORY_EXTRA, category);
		return intent;
	}

}

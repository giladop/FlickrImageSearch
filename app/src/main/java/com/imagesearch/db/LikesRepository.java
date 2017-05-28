package com.imagesearch.db;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.imagesearch.searchresults.model.data.ImageData;

import java.util.ArrayList;

import javax.inject.Inject;



/**
 * Created by giladopher on 10/05/2017.
 */
public class LikesRepository implements LikesRepositoryContract{



	private FirebaseAuth firebaseAuth;


	private DatabaseReference db;


	@Inject
	public LikesRepository(FirebaseAuth firebaseAuth, DatabaseReference db){
		this.firebaseAuth = firebaseAuth;
		this.db = db;
	}


	@Override
	public void getUserLike(@NonNull String userId, @NonNull LikesCallBack callBack){
		if (db.child("users") == null || db.child("users").child(userId) == null)
			callBack.onLikesLoaded(new ArrayList<>());
		else{
			db.child("users").child(userId);
		}
	}


	@Override
	public void getAllLikes(@NonNull LikesCallBack callBack){

	}


	@Override
	public void like(@NonNull String userId, @NonNull ImageData data){

	}


	@Override
	public void unLike(@NonNull String userId, @NonNull String imageDataId){

	}

}

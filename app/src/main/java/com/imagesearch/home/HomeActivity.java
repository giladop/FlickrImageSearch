package com.imagesearch.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.imagesearch.FlickerImageSearchApplication;
import com.imagesearch.R;
import com.imagesearch.di.DaggerFlickerImagesComponent;
import com.imagesearch.di.FlickerImagesComponent;
import com.imagesearch.di.FlickerImagesModule;
import com.imagesearch.search.view.SearchActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;



public class HomeActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener,
		GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{


	private static final int RC_SIGN_IN = 9001;


	private static final String TAG = "HomeActivity";


	private GoogleApiClient mGoogleApiClient;


	private FirebaseAuth mFirebaseAuth;


	private DatabaseReference db;


	@BindView(R.id.toolbar)
	Toolbar toolbar;


	@BindView(R.id.nav_view)
	NavigationView navigationView;


//	@BindView(R.id.userImage)
	ImageView userImage;


//	@BindView(R.id.userName)
	TextView userName;


//	@BindView(R.id.userEmail)
	TextView userEmail;


	TextView signInSignOut;


	@Inject
	Picasso picasso;


	/**
	 * Dagger injection.
	 */
	private void inject(){
		FlickerImagesComponent flickerImagesComponent =
				DaggerFlickerImagesComponent.builder()
						.appComponent(FlickerImageSearchApplication.getFlickerImageSearchApplication().getAppComponent())
						.flickerImagesModule(new FlickerImagesModule())
						.build();
		flickerImagesComponent.inject(this);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ButterKnife.bind(this);
		inject();

		setSupportActionBar(toolbar);

		View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);
		userImage = (ImageView)headerView.findViewById(R.id.userImage);
		userName = (TextView)headerView.findViewById(R.id.userName);
		userEmail = (TextView)headerView.findViewById(R.id.userEmail);
		signInSignOut = (TextView)headerView.findViewById(R.id.signInSignOut);
		signInSignOut.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				signInOrSignOut();
			}
		});

		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(view -> startActivity(SearchActivity.createStartIntent(HomeActivity.this)));

		DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		setup();

		// Initialize FirebaseAuth
		mFirebaseAuth = FirebaseAuth.getInstance();
		db = FirebaseDatabase.getInstance().getReference();
	}


	@Override
	protected void onStart(){
		super.onStart();
		setupUser(mFirebaseAuth.getCurrentUser());
	}



	private void signInOrSignOut(){
		if (mFirebaseAuth.getCurrentUser() == null)
			signIn();
		else
			signOut();
	}


	private void signOut(){
		Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>(){
			@Override
			public void onResult(@NonNull Status status){
				if (status.isSuccess()){
					mFirebaseAuth.signOut();
					setupUser(null);
				}
			}
		});
	}



	private void setupUser(FirebaseUser currentUser){
		if (currentUser == null) {
			userImage.setImageResource(R.drawable.ic_person_white_24dp);
			userName.setText(R.string.default_user_name);
			userEmail.setText("");
			signInSignOut.setText(R.string.sign_in);
		}else{
			picasso.load(currentUser.getPhotoUrl()).into(userImage);
			userName.setText(currentUser.getDisplayName());
			userEmail.setText(currentUser.getEmail());
			signInSignOut.setText(R.string.sign_out);
			getFavorites();
		}


	}

	private void getFavorites(){
		ValueEventListener postListener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				// Get Post object and use the values to update the UI
				Favorites favorites = dataSnapshot.getValue(Favorites.class);
				Log.d("stop", "stop");
				// ...
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				// Getting Post failed, log a message
				Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
				// ...
			}
		};
		db.addValueEventListener(postListener);
	}


	private void setup(){
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this )
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
	}


	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				// Google Sign In was successful, authenticate with Firebase
				GoogleSignInAccount account = result.getSignInAccount();
				firebaseAuthWithGoogle(account);
			} else {
				// Google Sign In failed
				Log.e(TAG, "Google Sign In failed.");
			}
		}
	}

	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		mFirebaseAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							Log.w(TAG, "signInWithCredential", task.getException());
							Toast.makeText(HomeActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(HomeActivity.this, "Authentication successed.",
									Toast.LENGTH_SHORT).show();

							setupUser(mFirebaseAuth.getCurrentUser());
						}
					}
				});
	}


	@Override
	public void onBackPressed(){
		DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)){
			drawer.closeDrawer(GravityCompat.START);
		} else{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings){
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item){
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera){
			signIn();

		} else if (id == R.id.nav_gallery){

		} else if (id == R.id.nav_slideshow){

		} else if (id == R.id.nav_manage){

		} else if (id == R.id.nav_share){

		} else if (id == R.id.nav_send){

		}

		DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}



	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
		Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(@Nullable Bundle bundle){
		Toast.makeText(this, "Google Play Services Connected.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionSuspended(int i){
		Toast.makeText(this, "Google Play Services Suspended.", Toast.LENGTH_SHORT).show();
	}
}

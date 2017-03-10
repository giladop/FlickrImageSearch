package com.imagesearch.searchresults.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.imagesearch.FlickerImageSearchApplication;
import com.imagesearch.R;
import com.imagesearch.di.DaggerFlickerImagesComponent;
import com.imagesearch.di.FlickerImagesComponent;
import com.imagesearch.di.FlickerImagesModule;
import com.imagesearch.searchresults.model.data.ImageData;
import com.squareup.picasso.Picasso;

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


	@Inject
	Picasso picasso;


	@BindView(R.id.toolbar)
	Toolbar toolbar;


	@BindView(R.id.image_view)
	ImageView imageView;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_screen_image_layout);
		ButterKnife.bind(this);
		inject();

		ImageData imageData = getIntent().getParcelableExtra(IMAGE_DATA_EXTRA);
		setupUi(imageData);
	}


	/**
	 * Setup the fullscreen image and title.
	 */
	private void setupUi(@NonNull ImageData imageData){
		picasso.load(imageData.getImageUrl()).into(imageView);
		toolbar.setTitle(imageData.title);
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
	public static Intent newIntent(@NonNull Context context, @NonNull ImageData imageData){
		Intent intent = new Intent(context, FullScreenImageActivity.class);
		intent.putExtra(IMAGE_DATA_EXTRA, imageData);
		return intent;
	}

}

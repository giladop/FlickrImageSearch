package com.imagesearch.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.imagesearch.FlickerImageSearchApplication;
import com.imagesearch.R;
import com.imagesearch.di.DaggerFlickerImagesComponent;
import com.imagesearch.di.FlickerImagesComponent;
import com.imagesearch.di.FlickerImagesModule;
import com.imagesearch.model.data.ImageData;
import com.imagesearch.presenter.FlickerImagesPresenterViewContract;
import com.imagesearch.presenter.FlickerImagesSearchPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * This is the main {@link Activity} of this app and act as the VIEW in the MVP pattern.
 * For simplify the activity hosts both search and list fragments and delegate actions/data to proper fragment.
 *
 */
public class MainImagesActivity extends AppCompatActivity
		implements FlickerImagesPresenterViewContract.View, SearchImagesFragment.SearchImagesCallback,
		ImagesListFragment.ImagesListListener{


	private static final String IMAGES_FRAGMENT_TAG = "image_fragment_tag";


	private static final String SEARCH_FRAGMENT_TAG = "search_fragment_tag";


	@BindView(R.id.fab)
	FloatingActionButton fab;


	/**
	 * This is the PRESENTER in MVP pattern. For simplify,
	 * the presenter handles/manages both search and list views.
	 */
	@Inject
	FlickerImagesSearchPresenter flickerImagesSearchPresenter;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_images);
		ButterKnife.bind(this);
		inject();

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FragmentManager fm = getFragmentManager();

		//setup presenter
		setupAndBindPresenter(fm);

		// Add first fragment
		if (savedInstanceState == null){
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(R.id.fragment_container, SearchImagesFragment.newInstance(), SEARCH_FRAGMENT_TAG);
			ft.commit();
		}

		//delegate FAB click to current representing fragment
		fab.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				((BaseFlickerFragment)getFragmentManager().findFragmentById(R.id.fragment_container)).onFabClicked();
			}
		});
	}


	/**
	 * create/recreate {@link FlickerImagesSearchPresenter} with help of {@link PresenterHolder} the presenter
	 * Survive configuration change.
	 */
	private void setupAndBindPresenter(FragmentManager fm){
		PresenterHolder presenterHolder = (PresenterHolder) fm.findFragmentByTag("presenterHolder");
		if (presenterHolder == null) {

			fm.beginTransaction().add(new PresenterHolder(), "presenterHolder").commit();
			fm.executePendingTransactions();
			inject();
		}else
			flickerImagesSearchPresenter = presenterHolder.getFlickerImagesSearchPresenter();

		if (flickerImagesSearchPresenter == null)
			inject();

		flickerImagesSearchPresenter.bind(this);
	}


	/**
	 * Save {@link FlickerImagesSearchPresenter}.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState){

		PresenterHolder presenterHolder = (PresenterHolder) getFragmentManager().findFragmentByTag("presenterHolder");
		if (presenterHolder != null)
			presenterHolder.setFlickerImagesSearchPresenter(flickerImagesSearchPresenter);

		super.onSaveInstanceState(outState);
	}


	/**
	 * Bind view (this) with presenter.
	 */
	@Override
	protected void onStart(){
		super.onStart();
		flickerImagesSearchPresenter.bind(this);
	}


	/**
	 * Un-Bind view (this) from presenter.
	 */
	@Override
	protected void onStop(){
		super.onStop();
		flickerImagesSearchPresenter.unbind();
	}


	/**
	 * Dagger injection.
	 */
	private void inject(){
		FlickerImagesComponent flickerImagesComponent =
				DaggerFlickerImagesComponent.builder()
						.apiComponent(FlickerImageSearchApplication.getFlickerImageSearchApplication(this).getApiComponent())
						.flickerImagesModule(new FlickerImagesModule())
						.build();
		flickerImagesComponent.inject(this);
	}


	@Override
	public void showLoadingImagesProgressIndicator(){
		ImagesListFragment imagesListFragment = getImagesFragment();
		if (imagesListFragment != null)
			imagesListFragment.showLoadingImagesProgressIndicator();
	}


	@Override
	public void hideLoadingImagesProgressIndicator(){
		ImagesListFragment imagesListFragment = getImagesFragment();
		if (imagesListFragment != null)
			imagesListFragment.hideLoadingImagesProgressIndicator();
	}


	@Override
	public void onImagesLoaded(List<ImageData> images){
		ImagesListFragment imagesListFragment = getImagesFragment();
		if (imagesListFragment != null)
			imagesListFragment.onImagesLoaded(images);
	}


	@Override
	public void onHistoryLoaded(List<String> history){
		SearchImagesFragment searchImagesFragment = getHistoryFragment();
		if (searchImagesFragment != null)
			searchImagesFragment.onHistoryLoaded(history);
	}


	private ImagesListFragment getImagesFragment(){
		return  (ImagesListFragment)getFragmentManager().findFragmentByTag(IMAGES_FRAGMENT_TAG);
	}


	private SearchImagesFragment getHistoryFragment(){
		return  (SearchImagesFragment)getFragmentManager().findFragmentByTag(SEARCH_FRAGMENT_TAG);
	}


	@Override
	public void onImagesNotFound(){
		ImagesListFragment imagesListFragment = getImagesFragment();
		if (imagesListFragment != null)
			imagesListFragment.onImagesNotFound();
	}


	@Override
	public void invokeSearch(@NonNull String query){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, ImagesListFragment.newInstance(query), IMAGES_FRAGMENT_TAG);
		ft.addToBackStack(null);
		ft.commit();
	}


	@Override
	public void reloadHistory(){
		flickerImagesSearchPresenter.getHistory();
	}


	@Override
	public void clearRecentSearch(){
		flickerImagesSearchPresenter.clearHistory();
	}


	@Override
	public void onLoadMore(String query, int page){
		flickerImagesSearchPresenter.getImages(query, page);
	}

}

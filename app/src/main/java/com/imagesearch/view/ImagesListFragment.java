package com.imagesearch.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imagesearch.R;
import com.imagesearch.model.data.ImageData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * A fragment showing a list images in Grid layout. The list supports pagination.
 * In addition, pull-to-refresh and click-to-full-screen-image are also supported.
 * The query received via {@link Intent}.
 *
 * @author Gilad Opher
 */
public class ImagesListFragment extends Fragment{


	private static final String QUERY_EXTRA = "queryExtra";


	@BindView(R.id.root)
	View view;


	@BindView(R.id.swipe_refresh_layout)
	SwipeRefreshLayout swipeRefreshLayout;


	@BindView(R.id.images_list)
	RecyclerView imagesList;


	@BindView(R.id.toolbar)
	Toolbar toolbar;


	@BindView(R.id.fab)
	FloatingActionButton fab;


	/**
	 * The images adapter.
	 */
	private ImagesAdapter imagesAdapter;


	/**
	 * callback to parent {@link Activity}.
	 */
	private ImagesListListener listener;


	/**
	 * Save the current page.
	 */
	private int currentPage;


	/**
	 * Images data list.
	 */
	private ArrayList<ImageData> images;


	@Override
	public void onAttach(Context context){
		super.onAttach(context);

		if (context instanceof ImagesListListener)
			this.listener = (ImagesListListener)context;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.images_list_layout, container, false);
		ButterKnife.bind(this, view);

		toolbar.setTitle(getArguments().getString(QUERY_EXTRA));

		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
		final EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
				currentPage = page;
				loadMore();
			}
		};

		swipeRefreshLayout.setOnRefreshListener(() -> {
			scrollListener.resetState();
			imagesAdapter.refresh();
			currentPage = 1;
			loadMore();
		});


		imagesList.setLayoutManager(gridLayoutManager);



		imagesAdapter = new ImagesAdapter(getActivity(), this::openFullScreenImageView);

		imagesList.setAdapter(imagesAdapter);
		imagesList.addOnScrollListener(scrollListener);

		if (savedInstanceState != null){
			currentPage = savedInstanceState.getInt("page");
			scrollListener.setCurrentPage(currentPage);
			images = savedInstanceState.getParcelableArrayList("images");
			imagesAdapter.addImages(images);
		}else{
			images = new ArrayList<>();
			currentPage = 1;
			loadMore();
		}

		fab.setOnClickListener(v -> onFabClicked());

		return view;
	}


	/**
	 * Start full screen image activity with transition animation.
	 */
	private void openFullScreenImageView(ImageData imageData, ImageView imageView){
		Intent startIntent = FullScreenImageActivity.newIntent(getActivity(), imageData);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
					getActivity(),
					imageView,
					"transition_photo")
					.toBundle();
			startActivity(startIntent, bundle);
		}else
			startActivity(startIntent);
	}


	@Override
	public void onSaveInstanceState(Bundle outState){
		outState.putInt("page", currentPage);
		outState.putParcelableArrayList("images", images);
		super.onSaveInstanceState(outState);
	}


	private void loadMore(){
		String query = getArguments().getString(QUERY_EXTRA);
		if (listener != null)
			listener.onLoadMore(query, currentPage);
	}


	public void onImagesLoaded(List<ImageData> images){
		this.images.addAll(images);
		imagesAdapter.addImages(images);
	}


	public void onFabClicked(){
		getActivity().getFragmentManager().popBackStack();
	}


	public void showLoadingImagesProgressIndicator(){
		if (!swipeRefreshLayout.isRefreshing())
			swipeRefreshLayout.setRefreshing(true);
	}


	public void hideLoadingImagesProgressIndicator(){
		swipeRefreshLayout.setRefreshing(false);
	}


	public void onImagesNotFound(){
		Snackbar.make(view, R.string.no_images_found, Snackbar.LENGTH_LONG).show();
	}


	/**
	 * Reference to parent {@link Activity}.
	 */
	public interface ImagesListListener{


		/**
		 * Invoke when detecting new page load.
		 */
		void onLoadMore(String query, int page);
	}


	/**
	 * Create a {@link ImagesListFragment} instance.
	 */
	public static ImagesListFragment newInstance(@NonNull String query){
		ImagesListFragment fragment = new ImagesListFragment();

		Bundle args = new Bundle();
		args.putString(QUERY_EXTRA, query);
		fragment.setArguments(args);

		return fragment;
	}

}

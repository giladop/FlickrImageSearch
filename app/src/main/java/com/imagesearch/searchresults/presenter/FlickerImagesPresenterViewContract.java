package com.imagesearch.searchresults.presenter;

import com.imagesearch.searchresults.model.data.ImageData;
import com.imagesearch.searchresults.view.SearchResultsActivity;

import java.util.List;



/**
 * The contract between {@link FlickerImagesSearchPresenter} and it's corresponding views.
 * For simplify, the view {@link SearchResultsActivity} delegate data to corresponding fragments.
 *
 * @author Gilad Opher
 */
public interface FlickerImagesPresenterViewContract{


	/**
	 * The methods the {@link View} expose to the {@link FlickerImagesSearchPresenter}.
	 */
	interface View{


		void showLoadingImagesProgressIndicator();


		void hideLoadingImagesProgressIndicator();


		void onImagesLoaded(List<ImageData> images);


		void onImagesNotFound();
	}


	/**
	 * The methods the {@link FlickerImagesSearchPresenter} expose to the {@link View}.
	 */
	interface UserActionsListener{


		void getImages(String query, int page);


		void bind(View v);


		void unbind(boolean strong);
	}

}

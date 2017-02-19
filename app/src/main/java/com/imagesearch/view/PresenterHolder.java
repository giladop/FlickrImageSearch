package com.imagesearch.view;

import android.app.Fragment;
import android.os.Bundle;

import com.imagesearch.presenter.FlickerImagesSearchPresenter;



/**
 * A helper {@link Fragment} that keeps {@link FlickerImagesSearchPresenter} alive during configuration change.
 *
 * @author Gilad Opher
 */
public class PresenterHolder extends Fragment{


	/**
	 * The presenter.
	 */
	private FlickerImagesSearchPresenter flickerImagesSearchPresenter;


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}


	/**
	 * Setter
	 */
	public void setFlickerImagesSearchPresenter(FlickerImagesSearchPresenter flickerImagesSearchPresenter){
		this.flickerImagesSearchPresenter = flickerImagesSearchPresenter;
	}


	/**
	 * Getter
	 */
	public FlickerImagesSearchPresenter getFlickerImagesSearchPresenter(){
		return flickerImagesSearchPresenter;
	}

}

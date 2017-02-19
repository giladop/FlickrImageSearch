package com.imagesearch.view;

import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;



/**
 * This interface is implemented by {@link Fragment}'s that presents on the main activity screen
 * and share the same {@link FloatingActionButton}.
 *
 * @author Gilad Opher
 */
public interface BaseFlickerFragment{


	/**
	 * invokes when {@link FloatingActionButton} was clicked.
	 */
	void onFabClicked();

}

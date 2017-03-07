package com.imagesearch.search.presenter;

import java.util.List;



/**
 * @author Gilad Opher
 */
public interface SearchPresenterViewContract {


	interface View{



		void onHistoryLoaded(List<String> history);

	}

	interface UserActionsListener{


		void onNewQuery(String query);


		void getHistory();


		void clearHistory();


		void bind(SearchPresenterViewContract.View v);


		void unbind();
	}
}

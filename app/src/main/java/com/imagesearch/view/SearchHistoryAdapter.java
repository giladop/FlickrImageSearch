package com.imagesearch.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imagesearch.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Recent search queries {@link RecyclerView.Adapter}
 *
 * @author Gilad Opher
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>{


	/**
	 * The recent search history list.
	 */
	private List<String> history;


	/**
	 * A callback allowing delegate recent search click backwards to {@link SearchImagesFragment}.
	 */
	private SearchHistoryCallback callback;


	SearchHistoryAdapter(SearchHistoryCallback callback){
		this.callback = callback;
	}


	public void onHistoryLoaded(List<String> history){
		this.history = new ArrayList<>(history);
		notifyDataSetChanged();
	}


	public void clearHistory(){
		history.clear();
		notifyDataSetChanged();
	}


	/**
	 * The item view click listener.
	 */
	private final View.OnClickListener clickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v){
			HistoryViewHolder historyViewHolder = (HistoryViewHolder)v.getTag();
			onItemClick(historyViewHolder);
		}
	};


	/**
	 * Handle recent search item click listener.
	 */
	private void onItemClick(HistoryViewHolder historyViewHolder){
		if (callback == null) return;

		int position = historyViewHolder.getAdapterPosition();
		if (position == RecyclerView.NO_POSITION)
			return;

		String query = history.get(position);
		callback.onRecentSearchClicked(query);
	}


	@Override
	public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_cell_layout, parent, false);
		view.setOnClickListener(clickListener);
		HistoryViewHolder holder = new HistoryViewHolder(view);
		view.setTag(holder);

		return holder;
	}


	@Override
	public void onBindViewHolder(HistoryViewHolder holder, int position){
		String recent = history.get(position);
		holder.recentSearch.setText(recent);
	}


	@Override
	public int getItemCount(){
		return history != null ? history.size() : 0;
	}


	/**
	 * The recent search {@link RecyclerView.ViewHolder}.
	 */
	class HistoryViewHolder extends RecyclerView.ViewHolder{


		@BindView(R.id.recent_search)
		TextView recentSearch;


		HistoryViewHolder(View itemView){
			super(itemView);

			ButterKnife.bind(this, itemView);
		}
	}


	/**
	 * A callback for external use
	 */
	interface SearchHistoryCallback{


		/**
		 * Invoke when click on recent search item.
		 */
		void onRecentSearchClicked(String query);
	}

}

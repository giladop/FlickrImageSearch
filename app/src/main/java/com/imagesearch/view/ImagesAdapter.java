package com.imagesearch.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imagesearch.FlickerImageSearchApplication;
import com.imagesearch.R;
import com.imagesearch.model.data.ImageData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * A simple {@link RecyclerView.Adapter} adapter showing {@link ImageView}.
 *
 * @author Gilad Opher
 */
class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>{


	/**
	 * Images data list.
	 */
	private List<ImageData> images;


	/**
	 * The {@link Picasso} image loader.
	 */
	private Picasso picasso;


	/**
	 * The current total images that was added to adapter.
	 */
	private int total = 0;


	/**
	 * A callback allowing delegate image click backwards to {@link ImagesListFragment}.
	 */
	private ImagesCallback imagesCallback;


	ImagesAdapter(Context context, ImagesCallback imagesCallback){
		this.images = new ArrayList<>();
		this.picasso = FlickerImageSearchApplication.getFlickerImageSearchApplication(context).getAppComponent().picasso();
		this.imagesCallback = imagesCallback;
	}


	/**
	 * Add more images to list.
	 */
	void addImages(List<ImageData> newImages){
		images.addAll(newImages);
		notifyItemRangeInserted(total, newImages.size());
		total = total + newImages.size();
	}


	/**
	 * Clear list on refresh.
	 */
	void refresh(){
		images.clear();
		notifyDataSetChanged();
	}


	/**
	 * The item view click listener.
	 */
	private final View.OnClickListener clickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v){
			ImageViewHolder imageViewHolder = (ImageViewHolder)v.getTag();
			onItemClick(imageViewHolder);
		}
	};


	/**
	 * On image clicked, delegate image data and view to callback.
	 */
	private void onItemClick(ImageViewHolder imageViewHolder){
		if (imagesCallback == null) return;

		int position = imageViewHolder.getAdapterPosition();
		if (position == RecyclerView.NO_POSITION)
			return;

		ImageData imageData = images.get(position);
		imagesCallback.onImageClicked(imageData, imageViewHolder.imageView);
	}


	@Override
	public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_cell_layout, parent, false);
		view.setOnClickListener(clickListener);
		ImageViewHolder holder = new ImageViewHolder(view);
		view.setTag(holder);

		return holder;
	}


	@Override
	public void onBindViewHolder(ImageViewHolder holder, int position){
		ImageData imageData = images.get(position);
		picasso.load(imageData.getImageUrl()).into(holder.imageView);
	}


	@Override
	public int getItemCount(){
		return images != null ? images.size() : 0;
	}



	/**
	 * The Image {@link RecyclerView.ViewHolder}.
	 */
	class ImageViewHolder extends RecyclerView.ViewHolder{


		@BindView(R.id.image_view)
		ImageView imageView;


		ImageViewHolder(View itemView){
			super(itemView);

			ButterKnife.bind(this, itemView);
		}

	}


	/**
	 * A callback for external use
	 */
	interface ImagesCallback{


		/**
		 * Invoke when click on image item.
		 */
		void onImageClicked(ImageData imageData, ImageView imageView);
	}

}
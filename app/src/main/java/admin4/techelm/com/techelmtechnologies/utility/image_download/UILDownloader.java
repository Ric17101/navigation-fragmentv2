package admin4.techelm.com.techelmtechnologies.utility.image_download;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import admin4.techelm.com.techelmtechnologies.R;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.HTTP_AUTHENTICATION_ACCESS;

/**
 * Created 27/04/2017 by admin4
 * Call UILDownloader using:
 	UILDownloader imageLoader = new UILDownloader(context, imageURL).setImageView(imageView);
 	imageLoader.start();
 * Must be implement First on your Activity
 	implements UILDownloader
 * ORRRRRRR
     private void downloadImage() {
         UILDownloader downloader = new UILDownloader(DownloadImageActivity.this);
         downloader.setImageFrom(IMAGE_URL);
         downloader.setImageView(imageView);
         downloader.start();
     }
 */
public class UILDownloader {

	private static final String TAG = UILDownloader.class.getSimpleName();

	private UILDownloader mDownloader;
	private ImageView mImageView;
	private File cacheDir;
	private String mImageFrom;
	private Context mContext;
	private DisplayImageOptions mOptions;
	private ImageLoaderConfiguration mConfig;
	private ImageLoader mImageLoader;
	private UILListener mCallback;

    private UILDownloader() {}
	/**
	 * @param context - Calling Activity
	 */
	public UILDownloader(Context context) {
		this.mContext = context;
        this.mDownloader = new UILDownloader();

		try {
			this.mCallback = (UILListener) context;
            this.initUIL();
		} catch (ClassCastException ex) {
			Log.e(TAG, "Must implement the ProjectJobListener in the Activity", ex);
		}
    }
    /**
     * @param imageURL - Can be from URL, Drawables or Local Storage
     */
	public void setImageFrom(String imageURL) {
        this.mImageFrom = imageURL;
    }

    /**
     * @param imageView - ImageView you are setting....
     */
	public void setImageView(ImageView imageView) {
		this.mImageView = imageView;
	}

	private DisplayImageOptions setImageOptions() {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.mipmap.ic_empty)
				.showImageOnFail(R.mipmap.ic_error)
				.resetViewBeforeLoading(true)
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300))
				.build();
		return options;
	}

	private ImageLoaderConfiguration setConfig() {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this.mContext);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.imageDownloader(new UILCustomDownloader(this.mContext));
		config.writeDebugLogs(); // Remove for release app

		return config.build();
	}

	private void initUIL() {

		this.mOptions = setImageOptions();

		this.mImageLoader = ImageLoader.getInstance();

        ImageLoaderConfiguration config = setConfig();

		this.mImageLoader.init(config);

        // Initialize ImageLoader with configuration.
        // ImageLoader.getInstance().init(config);
	}

	public void start() {
		if (mImageView != null && mOptions != null && mImageFrom != null) {
			this.mImageLoader.displayImage(mImageFrom, mImageView, this.mOptions, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					Log.e(TAG, "onLoadingStarted " + imageUri);
					mCallback.OnHandleStartDownload("onLoadingStarted " + imageUri);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR: message = "Input/Output error"; break;
						case DECODING_ERROR: message = "Image can't be decoded"; break;
						case NETWORK_DENIED: message = "Downloads are denied"; break;
						case OUT_OF_MEMORY: message = "Out Of Memory error"; break;
						case UNKNOWN: message = "Unknown error"; break;
						default: message = "null"; break;
					}
					Log.e(TAG, "onLoadingFailed " + message);
					Log.e(TAG, "onLoadingFailed " + failReason.getType());
					mCallback.OnHandleError("onLoadingFailed " + message);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// mImageView.setImageBitmap(loadedImage);
					Log.e(TAG, "onLoadingComplete " + imageUri);
                    // This is not intended to set image from the implementing Activity
                    mCallback.OnHandleLoadingCompleted(imageUri, loadedImage);
				}
			});
		} else {
			mCallback.OnHandleError("onLoadingFailed " + "Something went wrong.");
		}
	}

	// Sample 1
	// This configuration tuning is custom. You can tune every option, you may tune some of them,
	// or you can create default configuration by
	//  ImageLoaderConfiguration.createDefault(this);
	// method.
	public static void initConfiguration(Context context) {

		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		// config.imageDownloader(new BaseImageDownloader(context)); // default
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}

	// Sample 2
	// Not Used
	public void setCacheDir() {
		cacheDir = StorageUtils.getCacheDirectory(this.mContext);
	}
	// Not used
	private void setConfig_() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.mContext)
				.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null)
//                .taskExecutor(...)
//                .taskExecutorForCachedImages(...)
				.threadPoolSize(3) // default
				.threadPriority(Thread.NORM_PRIORITY - 2) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO) // default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(13) // default
				.diskCache(new UnlimitedDiskCache(cacheDir)) // default
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				//.imageDownloader(new BaseImageDownloader(this.mContext)) // default
				.imageDecoder(new BaseImageDecoder(true)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs()
				.build();
		this.mConfig = config;
	}
}

/* SAMPLER only
	private void downloadImage() {
		File cacheDir = StorageUtils.getCacheDirectory(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null)
//                .taskExecutor(...)
//                .taskExecutorForCachedImages(...)
				.threadPoolSize(3) // default
				.threadPriority(Thread.NORM_PRIORITY - 2) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO) // default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(13) // default
				.diskCache(new UnlimitedDiskCache(cacheDir)) // default
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(this)) // default
				.imageDecoder(new BaseImageDecoder(true)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs()
				.build();

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.mipmap.ic_empty)
				.showImageOnFail(R.mipmap.ic_error)
				.resetViewBeforeLoading(true)
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300))
				.build();

		final ImageView imageView = (ImageView) findViewById(R.id.imageViewDownload);

		ImageLoader loader = ImageLoader.getInstance();
		loader.init(config);
		loader.displayImage(IMAGE_URL, imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				Log.e(TAG, "onLoadingStarted " + imageUri);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case DECODING_ERROR:
						message = "Image can't be decoded";
						break;
					case NETWORK_DENIED:
						message = "Downloads are denied";
						break;
					case OUT_OF_MEMORY:
						message = "Out Of Memory error";
						break;
					case UNKNOWN:
						message = "Unknown error";
						break;
				}
				Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
				Log.e(TAG, "onLoadingFailed " + message);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				imageView.setImageBitmap(loadedImage);
				Log.e(TAG, "onLoadingComplete " + imageUri);
			}
		});
	}


 */
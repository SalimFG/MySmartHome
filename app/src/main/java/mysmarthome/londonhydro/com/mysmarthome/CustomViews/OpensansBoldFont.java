package mysmarthome.londonhydro.com.mysmarthome.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import mysmarthome.londonhydro.com.mysmarthome.R;


public class OpensansBoldFont extends TextView {
	private Context mContext;
	private String mFont;

	public OpensansBoldFont(Context context) {
		super(context, null);
		mContext = context;
		init();
	}

	public OpensansBoldFont(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CustomButtom, 0, 0);
		try {
			mFont = a.getString(R.styleable.CustomButtom_font);
		} finally {
			a.recycle();
		}
		init();
	}

	private void init() {

		Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/opensansbold.ttf");
		setTypeface(typeFace);
	}

}

package google.zxing.sacn.comm;

import android.content.Context;

public class SysUtils {
	/**
	 * dip 转 pix
	 * 
	 * @param context
	 * @param dpValue
	 * @return pix
	 */
	public static int dipToPx(Context context, float dpValue) {
		final float d = 0.5f;
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + d);
	}
}

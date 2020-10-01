package com.android.fire;

import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

public class AnimatedView extends android.support.v7.widget.AppCompatImageView{
		private int width;
		private int vwidth = 0, vheight = 0;
		private int[] bmap, byteArray;
		private int[] palette = new int[256];
		private float scale = 4.0f;
		private final int height = 150;
		private Random rnd = new Random();
		private boolean initOk = false;
		
		public AnimatedView(Context context, AttributeSet attrs){
			super(context, attrs);
			int idx = 1, cl;
			for(cl = 8; cl > 0; cl--)   palette[idx++] = 0xff000000 | 16 - cl << 19;
			for(cl = 16; cl > 0; cl--)  palette[idx++] = 0xfff00000 | 16 - cl << 10;
			for(cl = 16; cl > 0; cl--)  palette[idx++] = 0xfff0f000 | 26 - cl << 2;
			for(cl = 215; cl > 0; cl--) palette[idx++] = 0xfffcfcfc;

//			for(cl = 0; cl < 256; cl++) palette[cl] = 0xff000000 | cl << 2 | cl << 10 | cl << 18;
		}

		protected void onDraw(Canvas c){
			if(!initOk) return;

			c.scale(scale, scale, 0.0f, vheight);

			for(int i = 0; i < width; i++) byteArray[(height - 2) * width + i] = (byte)rnd.nextInt(256);

			for(int i = 4 * width; i < (width * height) - 2 * width; i++){
				int a = byteArray[i];
				a+= byteArray[i + 1];
				a+= byteArray[i - 1];
				a+= byteArray[i + width + 1];
				a+= byteArray[i - width + 1];
				a+= byteArray[i + width];
				a+= byteArray[i - width];
				a+= byteArray[i - width];
				a = a >> 3 & 0xff;
				if(a != 0) a--;
				byteArray[i - 4 * width] = a;
				bmap[i] = palette[a];
			}

			c.drawBitmap(bmap, 0, width, 0, vheight - height + 24, width, height, false, null);

			invalidate();
		}

		@Override
		protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
			super.onSizeChanged(xNew, yNew, xOld, yOld);

//			Log.d("debug", "w = " + getWidth());
//			Log.d("debug", "h = " + getHeight());

			if(getWidth() != 0 && getHeight() != 0){
				vwidth = getWidth();
				vheight = getHeight();
				width = (int)(vwidth / scale);
				bmap = new int[width*height];
				byteArray = new int[width*height];
				initOk = true;
			}
		}
}
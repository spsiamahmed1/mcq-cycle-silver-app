package com.siam.mcqcycle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import java.util.Random;

public class ConfettiView extends View {
    private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random=new Random(42);
    public ConfettiView(Context c){super(c);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
    @Override protected void onDraw(Canvas canvas){super.onDraw(canvas);int[] colors={0xffff8a00,0xffff6b00,0xff46a36b,0xff4080d0,0xffffcc33};
        for(int i=0;i<90;i++){float x=random.nextFloat()*getWidth();float y=(random.nextFloat()*getHeight()+System.currentTimeMillis()/18f)%Math.max(1,getHeight());paint.setColor(colors[i%colors.length]);canvas.save();canvas.rotate((i*23)%360,x,y);canvas.drawRect(x,y,x+7,y+12,paint);canvas.restore();}}
}

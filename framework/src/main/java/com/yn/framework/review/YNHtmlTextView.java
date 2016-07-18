package com.yn.framework.review;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

/**
 * Created by youjiannuo on 16/6/14.
 */
public class YNHtmlTextView extends YNTextView {
    public YNHtmlTextView(Context context) {
        super(context);
    }

    public YNHtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(String startString, String value, String endString) {
        setText(startString + Html.fromHtml(value) + endString);
    }

//    public Spanned text(String value) {
//        return Html.fromHtml(value, null, new Html1());
//    }
//
//    class Html1 implements android.text.Html.TagHandler {
//
//
//        @Override
//        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
//            SystemUtil.printlnInfo("tag = " + tag + "   " + output.toString());
////            output.setSpan();
//        }
//    }

}

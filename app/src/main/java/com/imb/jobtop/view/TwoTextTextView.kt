package com.imb.jobtop.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.imb.jobtop.R
import kotlinx.android.synthetic.main.view_infin_two_color_text_view.view.*


class TwoTextTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {


    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_infin_two_color_text_view, this, true)


        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.TwoTextTextView, 0, 0
            )

            val textSize = typedArray
                .getFloat(
                    R.styleable
                        .TwoTextTextView_bothSize, 14f
                )

            val textStyle = typedArray.getInt(R.styleable.TwoTextTextView_bothStyle, 0)


            val firstColor = typedArray
                .getColor(
                    R.styleable
                        .TwoTextTextView_firstColor,
                    ContextCompat.getColor(context, R.color.blue)
                )

            val secondColor = typedArray
                .getColor(
                    R.styleable
                        .TwoTextTextView_secondColor,
                    ContextCompat.getColor(context, R.color.lightMagenta)
                )

            val firstTextContent = typedArray
                .getString(
                    R.styleable.TwoTextTextView_firstText
                )

            val secondTextContent = typedArray
                .getString(
                    R.styleable
                        .TwoTextTextView_secondText
                )

            text1.textSize = textSize
            text1.setTextColor(firstColor)
            text2.textSize = textSize
            text2.setTextColor(secondColor)
            //take
            text1.setTypeface(null, textStyle)
            text1.text = firstTextContent
            text2.text = secondTextContent

            typedArray.recycle()
        }

        orientation = HORIZONTAL
    }


    fun setFirstText(text: String) {
        text1.text = text
    }

    fun setSecondText(text: String) {
        text2.text = text
    }

    fun setFirstColor(color: Int){
        text1.setTextColor(ContextCompat.getColor(context, color))
    }

    fun setSecondColor(color: Int){
        text2.setTextColor(ContextCompat.getColor(context, color))
    }
}

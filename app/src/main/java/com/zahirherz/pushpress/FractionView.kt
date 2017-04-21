package com.zahirherz.pushpress

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import android.os.Bundle
import android.os.Parcelable
import com.zahirherz.pushpress.R.styleable.*


/**
 * Created by zahirh on 4/10/17.
 */
class FractionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val circlePaint: Paint = Paint().apply {
        color = Color.rgb(236,214,69)//#ecd645
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    val circleMaskPaint: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = 40f

    }
    val sectionPaint: Paint = Paint().apply {
        color = Color.rgb(116,21,172)//#7415ac
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    var timerText: String = "00:30"

    val sectionOval: RectF = RectF()

    var numerator: Int = 1
    var denominator: Int = 15

    var countDownTimer = object: CountDownTimer(15000,1000) {
        override fun onTick(millisecTillDone: Long) {
            updateTimer(millisecTillDone.toInt() / 1000)
        }

        override fun onFinish() {
            resetTimer()
        }
        /*
        fun onFinish() {
            resetTimer()
            //val mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alert)
            //mediaPlayer.start()
        }
        */
    }
    var timerIsActive = false

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom
        val size: Int = Math.min(width,height)
        val cx: Int = width / 2 + paddingLeft
        val cy: Int = height / 2 + paddingTop
        val radius: Int = size / 2
        canvas?.drawCircle(cx.toFloat(),cy.toFloat(),radius.toFloat(),circlePaint)

        sectionOval.top = (height - size).toFloat() / 2 + paddingTop
        sectionOval.left = (width - size).toFloat() / 2 + paddingLeft
        sectionOval.bottom = sectionOval.top + size
        sectionOval.right = sectionOval.left + size

        canvas?.drawArc(sectionOval,0f,getSweepAngle(),true,sectionPaint)

        canvas?.drawCircle(cx.toFloat(),cy.toFloat(),(radius - 10).toFloat(),circleMaskPaint)

        canvas?.drawText(timerText,cx.toFloat(),cy.toFloat(),circleMaskPaint)


    }

    private fun getSweepAngle(): Float {
        return numerator * 360f / denominator
    }

    fun setFraction(numerator: Int, denominator: Int) {

        if (numerator < 0) {
            return
        }
        if (denominator <= 0) {
            return
        }
        if (numerator > denominator) {
            return
        }

        //numerator = numerator
        //denominator = denominator

        // Request a redraw
        invalidate()

    }

    private fun startTimer() {
        if(!timerIsActive) {
            timerIsActive = true
            countDownTimer.start()
        } else {
            resetTimer()
        }
    }

    fun resetTimer() {
        countDownTimer.cancel()
        timerIsActive = false
    }

    fun updateTimer(any: Any) {
        var numerator = numerator++
        if (numerator > denominator) {
            numerator = 0
        }
        Log.d("/// updateTimer ",numerator.toString() + " " + denominator.toString() )
        setFraction(numerator, denominator)
    }

    public override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("numerator", numerator)
        bundle.putInt("denominator", denominator)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val bundle = state
            numerator = bundle.getInt("numerator")
            denominator = bundle.getInt("denominator")
            super.onRestoreInstanceState(bundle.getParcelable<Parcelable>("superState"))
        } else {
            super.onRestoreInstanceState(state)
        }
        setFraction(numerator, denominator)
    }
}
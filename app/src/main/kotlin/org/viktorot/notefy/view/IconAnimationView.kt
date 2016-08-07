package org.viktorot.notefy.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Point
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import org.viktorot.notefy.R

class IconAnimationView : FrameLayout {

    private var mIsSelected: Boolean

    init {
        mIsSelected = false
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun init() {
        View.inflate(context, R.layout.animation_item, this)

        val mNormalImage = findViewById(R.id.normalImage) as LinearLayout
        val mSelectedImage = findViewById(R.id.selectedImage) as LinearLayout

        val gestureListener = object: GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                val x: Int = if(e == null) 0 else e.x.toInt()
                val y: Int = if(e == null) 0 else e.y.toInt()

                mNormalImage.visibility = View.VISIBLE

                val animator: Animator
                if(mIsSelected) {
                    show(mNormalImage, x, y)
                }
                else {
                    hide(mNormalImage, x, y)
                }

                mIsSelected = !mIsSelected

                return true
            }
        }

        var gestureDetector = GestureDetector(context, gestureListener)

        mNormalImage.setOnTouchListener { view, motionEvent -> gestureDetector.onTouchEvent(motionEvent) }
        mSelectedImage.setOnTouchListener { view, motionEvent -> gestureDetector.onTouchEvent(motionEvent) }
    }

    fun show(target: View, x: Int, y: Int) {
        val animator = getRevealAnimator(target, x, y)
        animator.start()
    }

    fun hide(target: View, x: Int, y: Int) {
        val animator = getHideAnimator(target, x, y)
        animator.start()
    }

    private fun getRevealAnimator(target: View, touchX: Int, touchY: Int): Animator {
        val cx = target.width / 2
        val cy = target.height / 2

        val p1 = Point(touchX, touchY)
        val p2 = Point(cx, cy)

        val radius0 = Math.sqrt(Math.pow((p1.y - p2.y).toDouble(), 2.0) + Math.pow((p1.x - p2.x).toDouble(), 2.0)).toFloat()

        val radius: Float = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat() + radius0

        val animator = ViewAnimationUtils.createCircularReveal(target, touchX, touchY, 0f, radius)
        animator.interpolator = FastOutLinearInInterpolator()

        return animator
    }

    private fun getHideAnimator(target: View, touchX: Int, touchY: Int): Animator {
        val cx = target.width / 2
        val cy = target.height / 2

        val p1 = Point(touchX, touchY)
        val p2 = Point(cx, cy)

        val radius0 = Math.sqrt(Math.pow((p1.y - p2.y).toDouble(), 2.0) + Math.pow((p1.x - p2.x).toDouble(), 2.0)).toFloat()

        val radius: Float = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat() + radius0

        val animator = ViewAnimationUtils.createCircularReveal(target, touchX, touchY, radius, 0f)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                target.visibility = View.INVISIBLE
            }
        })
        animator.interpolator = FastOutLinearInInterpolator()

        return animator
    }


}
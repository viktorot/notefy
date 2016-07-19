package org.viktorot.notefy.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import org.viktorot.notefy.R
import org.viktorot.notefy.adapters.NumberedAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.notefy_activity_main)

//        var iconRecyclerView = findViewById(R.id.icon_grid) as RecyclerView
//        iconRecyclerView.addItemDecoration(MarginItemDecoration(10))
//        iconRecyclerView.layoutManager = GridLayoutManager(this, 2)
//        iconRecyclerView.adapter = NumberedAdapter(30)

//        val animator = DefaultItemAnimator()
//        animator.changeDuration = 1000
//
//        iconRecyclerView.itemAnimator = animator

        var selected: Boolean = true
        val target = findViewById(R.id.vju)
        val targetSelected = findViewById(R.id.vjuSelected)

        val gestureListener = object: GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                val x = if(e == null) 0 else e.x.toInt()
                val y = if(e == null) 0 else e.y.toInt()

                target.visibility = View.VISIBLE

                val animator: Animator
                if(selected) {
                    animator = getHideAnimator(target, x, y)
                }
                else {
                    animator = getRevealAnimator(target, x, y)
                }

                selected = !selected;
                animator.start()

                return true
            }
        }

        var gestureDetector = GestureDetector(this, gestureListener)


        target.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                return gestureDetector.onTouchEvent(p1)
            }
        })

        targetSelected.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                return gestureDetector.onTouchEvent(p1)
            }
        })

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.notefy_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        Snackbar.make(toolbar, "Jungle Boogie", Snackbar.LENGTH_LONG).show()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun animateElementSelect(targetView: View) {
        val cx = targetView.width / 2
        val cy = targetView.height / 2

        val radius: Float = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val animator = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, 0f, radius)

        targetView.visibility = View.VISIBLE
        animator.start()
    }

    private fun animateElementDeselect(targetView: View) {
        val cx = targetView.width / 2
        val cy = targetView.height / 2

        val radius: Float = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val animator = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, radius, 0f)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                targetView.visibility = View.INVISIBLE
            }
        })

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


private class MarginItemDecoration(size: Int) : RecyclerView.ItemDecoration() {
    private val margin: Int

    init {
        margin = size
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.set(margin, margin, margin, margin)
    }
}

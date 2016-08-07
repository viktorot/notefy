package org.viktorot.notefy.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import org.viktorot.notefy.R
import org.viktorot.notefy.notes_list.NotesListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)


        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            startListActivity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.notefy_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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

    private fun startListActivity() {
        val intent = Intent(this, NotesListActivity::class.java)
        startActivity(intent)
    }

}

